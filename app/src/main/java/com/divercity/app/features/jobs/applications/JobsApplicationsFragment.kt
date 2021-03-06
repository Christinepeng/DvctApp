package com.divercity.app.features.jobs.applications

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.features.jobs.ITabJobs
import com.divercity.app.features.jobs.applications.adapter.JobApplicationAdapter
import com.divercity.app.features.jobs.applications.adapter.JobApplicationViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class JobsApplicationsFragment : BaseFragment(), RetryCallback, ITabJobs {

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

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(JobsApplicationsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
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
//        viewModel.publishUnpublishJobResponse.observe(this, Observer { job ->
//            when (job?.status) {
//                Status.LOADING -> {
//                    showProgress()
//                }
//
//                Status.ERROR -> {
//                    hideProgress()
//                    Toast.makeText(activity, job.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                    adapter.currentList?.get(positionSaveUnsavedClicked)?.attributes?.isBookmarkedByCurrent =
//                            job.data?.attributes?.isBookmarkedByCurrent
//                    adapter.notifyItemChanged(positionSaveUnsavedClicked)
//                }
//            }
//        })

        viewModel.navigateToJobRecruiterDescription.observe(viewLifecycleOwner, Observer {

        })

        viewModel.navigateToJobSeekerDescription.observe(viewLifecycleOwner, Observer {
            it?.let { job ->
                navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id)
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedJobsList?.observe(this, Observer {
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

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE

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

    override fun fetchJobs(searchQuery: String?) {
        viewModel.fetchJobs(viewLifecycleOwner, searchQuery)
    }
}