package com.divercity.android.features.groups.onboarding

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.core.utils.OnboardingProgression
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.adapter.GroupsForOnboardingAdapter
import com.divercity.android.features.groups.adapter.GroupsForOnboardingViewHolder
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import com.divercity.android.model.position.GroupPosition
import com.divercity.android.features.groups.viewmodel.GroupViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_header_profile.btn_next
import kotlinx.android.synthetic.main.view_header_profile.btn_previous_page
import kotlinx.android.synthetic.main.view_header_profile.txt_title
import kotlinx.android.synthetic.main.view_header_profile.view.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectGroupViewModel

    lateinit var groupViewModel: GroupViewModel

    @Inject
    lateinit var adapter: GroupsForOnboardingAdapter

    private var countJoin = 0
    private var currentProgress: Int = 0

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

    private var lastGroupPositionTap = 0

    companion object {

        const val REQUEST_CODE_GROUP = 200

        fun newInstance() = SelectGroupFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectGroupViewModel::class.java]
        groupViewModel = ViewModelProviders.of(this, viewModelFactory)[GroupViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
        subscribeToJoinLiveData()
        setupHeader()
    }

    private fun setupHeader() {

        top_btn.visibility = View.VISIBLE
        include_search.visibility = View.GONE
        btn_close.visibility = View.GONE
        btn_continue.visibility = View.GONE
        btn_skip.visibility = View.GONE

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })

        include_header.apply {

            txt_title.setText(
                "Join some Communities!\n" +
                    "\n" +
                    "These communities are suggested based on\n" +
                    "the information you provided")
            txt_title.setTextSize(17F)
            txt_title.setLineSpacing(5F,1.4F)

            btn_next.setText("DONE")
//            checkContinueBtnState()

            btn_previous_page.setOnClickListener {
                navigator.navigateToSelectInterestsActivity(requireActivity())
            }

            btn_next.setOnClickListener {
                navigator.navigateToHomeActivity(requireActivity())
            }
        }
    }

    private fun search(query: String?) {
        if (lastSearch != query) {
            handlerSearch.removeCallbacksAndMessages(null)
            handlerSearch.postDelayed({
                viewModel.fetchGroups(this@SelectGroupFragment, query)
                subscribeToPaginatedLiveData()
                lastSearch = query
            }, AppConstants.SEARCH_DELAY)
        }
    }

    private fun subscribeToJoinLiveData() {

        groupViewModel.joinPublicGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
//                    adapter.reloadPosition(response.data!!.position)
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    adapter.updatePositionOnJoinPublicGroup(response.data!!)
//                    if (++countJoin == 3) {
//                        btn_next.setTextColor(Color.parseColor("#3197e4"))
//                        btn_next.isClickable = true
//                        btn_next.isEnabled = true
////                        btn_continue.background = ContextCompat.getDrawable(requireActivity(), R.drawable.shape_backgrd_round_blue2)
////                        btn_continue.setOnClickListener {
////                            val nextProgress = OnboardingProgression.getNextNavigationProgressOnboarding(requireActivity(), viewModel.accountType, currentProgress)
////                            navigator.navigateToHomeActivity(requireActivity(), nextProgress >= 100)
////                        }
//                    } else {
//                        btn_next.setTextColor(Color.parseColor("#763198e5"))
//                        btn_next.isClickable = false
//                        btn_next.isEnabled = false
//                    }
                }
            }
        })

        groupViewModel.requestToJoinPrivateGroupResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                    }

                    Status.ERROR -> {
                        adapter.reloadPosition(response.data!!.position)
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        adapter.updatePositionOnJoinPrivateGroupRequest(response.data!!)
                        countJoin++
//                        checkContinueBtnState()
                    }
                }
            })

        groupViewModel.leaveGroupResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                    }

                    Status.ERROR -> {
                        adapter.reloadPosition(response.data!!.position)
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        adapter.updatePositionOnLeaveGroup(response.data!!)
                        countJoin--
//                        checkContinueBtnState()
                    }
                }
            })
    }

    private fun checkContinueBtnState() {
        if (countJoin >= 3) {
            btn_next.setTextColor(Color.parseColor("#3197e4"))
            btn_next.isClickable = true
            btn_next.isEnabled = true
//            btn_continue.background = ContextCompat.getDrawable(requireActivity(), R.drawable.shape_backgrd_round_blue2)
//            btn_continue.setOnClickListener {
//                val nextProgress = OnboardingProgression.getNextNavigationProgressOnboarding(requireActivity(), viewModel.accountType, currentProgress)
//                navigator.navigateToHomeActivity(requireActivity(), nextProgress >= 100)
//            }
        } else {
            btn_next.setTextColor(Color.parseColor("#763198e5"))
            btn_next.isClickable = false
            btn_next.isEnabled = false
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedGroupList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let {
                    if (networkState.status == Status.SUCCESS && it.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener = object : GroupsForOnboardingViewHolder.Listener {

        override fun onGroupJoinClick(groupPosition: GroupPosition) {
            groupViewModel.joinGroup(groupPosition)
        }

        override fun onGroupRequestJoinClick(groupPosition: GroupPosition) {
            groupViewModel.requestToJoinGroup(groupPosition)
        }

        override fun onGroupLeaveClick(groupPosition: GroupPosition) {
            groupViewModel.leaveGroup(groupPosition)
        }

        override fun onGroupClick(position: Int, group: GroupResponse) {
            lastGroupPositionTap = position
            navigator.navigateToGroupDetailForResult(
                this@SelectGroupFragment,
                group,
                REQUEST_CODE_GROUP
            )
        }

    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP) {
            val group = adapter.currentList?.get(lastGroupPositionTap)
            if (group?.attributes?.isFollowedByCurrent == true || group?.isJoinRequestPending() == true) {
                countJoin++
                checkContinueBtnState()
            }
            adapter.notifyItemChanged(lastGroupPositionTap)
        }
    }
}