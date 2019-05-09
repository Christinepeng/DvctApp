package com.divercity.android.features.jobs.mypostings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.mypostings.adapter.JobsAdapter
import com.divercity.android.features.jobs.mypostings.adapter.JobsViewHolder
import com.divercity.android.features.search.ITabSearch
import kotlinx.android.synthetic.main.fragment_jobs_my_postings.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class MyJobsPostingsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: MyJobsPostingsViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var positionPublishClicked: Int = 0

    companion object {

        const val REQUEST_CODE_UPDATE = 150

        fun newInstance(): MyJobsPostingsFragment {
            return MyJobsPostingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs_my_postings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
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
        viewModel.publishJobResponse.observe(this, Observer { job ->
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
                        txt_no_job_postings.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        txt_no_job_postings.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    txt_no_job_postings.visibility = View.GONE
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

    private val listener: JobsViewHolder.Listener = object : JobsViewHolder.Listener {

        override fun onPublishClick(position: Int, job: JobResponse) {
            positionPublishClicked = position
            viewModel.publishJob(job)
        }

        override fun onJobClick(job: JobResponse) {
            navigator.navigateToJobDescriptionPosterForResultActivity(this@MyJobsPostingsFragment, REQUEST_CODE_UPDATE, job)
        }
    }

    override fun search(searchQuery: String?) {
        viewModel.fetchJobs(viewLifecycleOwner, searchQuery)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_UPDATE){
                isListRefreshing = true
                viewModel.refresh()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}