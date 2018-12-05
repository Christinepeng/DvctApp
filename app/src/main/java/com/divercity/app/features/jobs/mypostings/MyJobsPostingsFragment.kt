package com.divercity.app.features.jobs.mypostings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.ITabJobs
import com.divercity.app.features.jobs.mypostings.adapter.JobsAdapter
import com.divercity.app.features.jobs.mypostings.adapter.JobsViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class MyJobsPostingsFragment : BaseFragment(), RetryCallback, ITabJobs {

    lateinit var viewModelJobs: MyJobsPostingsViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var positionPublishClicked: Int = 0

    companion object {

        fun newInstance(): MyJobsPostingsFragment {
            return MyJobsPostingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelJobs = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(MyJobsPostingsViewModel::class.java)
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
        viewModelJobs.publishJobResponse.observe(this, Observer { job ->
            when (job?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, job.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.currentList?.get(positionPublishClicked)?.attributes?.publishable =
                            job.data?.attributes?.publishable
                    adapter.notifyItemChanged(positionPublishClicked)
                }
            }
        })

        viewModelJobs.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModelJobs.pagedJobsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModelJobs.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModelJobs.refreshState().observe(this, Observer { networkState ->

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
                viewModelJobs.refresh()
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
        viewModelJobs.retry()
    }

    private val listener: JobsViewHolder.Listener = object : JobsViewHolder.Listener {

        override fun onPublishClick(position: Int, job: JobResponse) {
            positionPublishClicked = position
            viewModelJobs.publishJob(job)
        }

        override fun onJobClick(job: JobResponse) {
            navigator.navigateToJobDescriptionPosterActivity(this@MyJobsPostingsFragment, job)
        }
    }

    override fun fetchJobs(searchQuery: String?) {
        viewModelJobs.fetchJobs(viewLifecycleOwner, searchQuery)
    }
}