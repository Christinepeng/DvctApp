package com.divercity.app.features.jobposting.sharetogroup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.features.jobposting.sharetogroup.adapter.ShareJobGroupAdapter
import kotlinx.android.synthetic.main.fragment_share_job_group.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class ShareJobGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ShareJobGroupViewModel

    @Inject
    lateinit var adapter: ShareJobGroupAdapter

    private var positionJoinClicked: Int = 0
    private var countJoin = 0
    private var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): ShareJobGroupFragment {
            val fragment = ShareJobGroupFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_share_job_group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ShareJobGroupViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCompanies(this, null)
        adapter.setRetryCallback(this)
//        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
        subscribeToJoinLiveData()
        setupHeader()
    }

    private fun setupHeader() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                var toSearch: String? = include_search.edtxt_search.getText().toString()

                if (toSearch == "")
                    toSearch = null

                viewModel.fetchCompanies(this@ShareJobGroupFragment, toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }
    }

    private fun subscribeToJoinLiveData() {
//        viewModel.joinGroupResponse.observe(this, Observer { school ->
//            when (school?.status) {
//                Status.LOADING -> showProgress()
//
//                Status.ERROR -> {
//                    hideProgress()
//                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                    // Updating join btn state
//                    adapter.currentList?.get(positionJoinClicked)?.attributes?.isIsFollowedByCurrent = true
//                    adapter.notifyItemChanged(positionJoinClicked)
//
//                    if (++countJoin == 3) {
//                        btn_continue.background = ContextCompat.getDrawable(activity!!, R.drawable.shape_backgrd_round_blue2)
//                        btn_continue.setOnClickListener {
//                            val nextProgress = OnboardingProgression.getNextNavigationProgressOnboarding(
//                                    activity!!, viewModel.accountType, currentProgress
//                            )
//                            navigator.navigateToHomeActivity(activity!!, nextProgress >= 100)
//                        }
//                    }
//                }
//            }
//        })
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

//    private val listener: ShareJobGroupViewHolder.Listener = ShareJobGroupViewHolder.Listener { position, group ->
//        positionJoinClicked = position
//        viewModel.joinGroup(group)
//    }
}