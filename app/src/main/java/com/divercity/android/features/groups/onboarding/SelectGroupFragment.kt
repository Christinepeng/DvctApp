package com.divercity.android.features.groups.onboarding

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.core.utils.OnboardingProgression
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsAdapter
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectGroupViewModel

    @Inject
    lateinit var adapter: GroupsAdapter

    private var positionJoinClicked: Int = -1
    private var positionJoinRequest: Int = -1

    private var countJoin = 0
    private var currentProgress: Int = 0

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

    companion object {
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
        viewModel.joinGroupResponse.observe(this, Observer { school ->
            when (school?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    // Updating join btn state
                    adapter.updatePositionOnJoinGroup(positionJoinClicked)

                    if (++countJoin == 3) {
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
            }
        })
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

    private val listener: GroupsViewHolder.Listener = object : GroupsViewHolder.Listener {

        override fun onGroupRequestJoinClick(position: Int, group: GroupResponse) {
            positionJoinRequest = position
            viewModel.requestToJoinGroup(group)
        }

        override fun onGroupClick(group: GroupResponse) {
        }

        override fun onGroupJoinClick(position: Int, group: GroupResponse) {
            positionJoinClicked = position
            viewModel.joinGroup(group)
        }
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}