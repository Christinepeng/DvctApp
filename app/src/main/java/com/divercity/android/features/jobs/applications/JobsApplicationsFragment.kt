package com.divercity.android.features.jobs.applications

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.jobs.applications.adapter.JobApplicationAdapter
import com.divercity.android.features.jobs.applications.adapter.JobApplicationViewHolder
import com.divercity.android.features.search.ITabSearch
import kotlinx.android.synthetic.main.fragment_jobs_applications.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class JobsApplicationsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: JobsApplicationsViewModel

    @Inject
    lateinit var adapter: JobApplicationAdapter

    private var isListRefreshing = false
    private var positionSaveUnsavedClicked: Int = 0

    companion object {

        fun newInstance(): JobsApplicationsFragment {
            return JobsApplicationsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs_applications

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(JobsApplicationsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeToRefresh()
        initAdapter()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
//        detailViewModel.publishUnpublishJobResponse.observe(this, Observer { companyName ->
//            when (companyName?.status) {
//                Status.LOADING -> {
//                    showProgress()
//                }
//
//                Status.ERROR -> {
//                    hideProgress()
//                    Toast.makeText(activity, companyName.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                    singleAdapter.currentList?.get(positionSaveUnsavedClicked)?.userAttributes?.isBookmarkedByCurrent =
//                            companyName.data?.userAttributes?.isBookmarkedByCurrent
//                    singleAdapter.notifyItemChanged(positionSaveUnsavedClicked)
//                }
//            }
//        })

        viewModel.navigateToJobRecruiterDescription.observe(viewLifecycleOwner, Observer {

        })

        viewModel.navigateToJobSeekerDescription.observe(viewLifecycleOwner, Observer {
            it?.let { job ->
                navigator.navigateToJobDescriptionSeekerActivity(requireActivity(), job.id, job)
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedJobsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    if (viewModel.lastSearch == null || viewModel.lastSearch == "") {
                        txt_no_job_applications.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        txt_no_job_applications.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    txt_no_job_applications.visibility = View.GONE
                    txt_no_results.visibility = View.GONE
                }

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                viewModel.refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener: JobApplicationViewHolder.Listener = object : JobApplicationViewHolder.Listener {

    }

    override fun search(searchQuery: String?) {
        viewModel.fetchJobs(viewLifecycleOwner, searchQuery)
    }
}