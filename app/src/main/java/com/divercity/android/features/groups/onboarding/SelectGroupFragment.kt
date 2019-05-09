package com.divercity.android.features.groups.onboarding

import android.content.Intent
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
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import com.divercity.android.model.position.GroupPositionModel
import com.divercity.android.features.groups.viewmodel.GroupViewModel
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectGroupViewModel

    lateinit var groupViewModel: GroupViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var countJoin = 0
    private var currentProgress: Int = 0

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

    private var lastGroupPositionTap = 0

    companion object {

        const val REQUEST_CODE_GROUP = 200
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectGroupFragment {
            val fragment = SelectGroupFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectGroupViewModel::class.java]
        groupViewModel = ViewModelProviders.of(this, viewModelFactory)[GroupViewModel::class.java]

        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
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

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_group)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
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
                    adapter.reloadPosition(response.data!!.position)
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    adapter.updatePositionOnJoinPublicGroup(response.data!!)
                    if (++countJoin == 3) {
                        btn_continue.background =
                            ContextCompat.getDrawable(
                                activity!!,
                                R.drawable.shape_backgrd_round_blue2
                            )
                        btn_continue.setOnClickListener {
                            val nextProgress =
                                OnboardingProgression.getNextNavigationProgressOnboarding(
                                    activity!!, viewModel.accountType, currentProgress
                                )
                            navigator.navigateToHomeActivity(activity!!, nextProgress >= 100)
                        }
                    }
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
                        adapter.updatePositionOnJoinRequest(response.data!!)
                        countJoin++
                        checkContinueBtnState()
                    }
                }
            })
    }

    private fun checkContinueBtnState() {
        if (countJoin >= 3) {
            btn_continue.background =
                ContextCompat.getDrawable(activity!!, R.drawable.shape_backgrd_round_blue2)
            btn_continue.setOnClickListener {
                val nextProgress = OnboardingProgression.getNextNavigationProgressOnboarding(
                    activity!!, viewModel.accountType, currentProgress
                )
                navigator.navigateToHomeActivity(activity!!, nextProgress >= 100)
            }
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

    private val listener = object : GroupsViewHolder.Listener {

        override fun onGroupRequestJoinClick(groupPosition: GroupPositionModel) {
            groupViewModel.requestToJoinGroup(groupPosition)
        }

        override fun onGroupJoinClick(groupPosition: GroupPositionModel) {
            groupViewModel.joinGroup(groupPosition)
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