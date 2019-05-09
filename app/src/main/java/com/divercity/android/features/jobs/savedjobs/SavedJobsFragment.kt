package com.divercity.android.features.jobs.savedjobs

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.jobs.jobs.adapter.JobsAdapter
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import com.divercity.android.features.search.ITabSearch
import kotlinx.android.synthetic.main.fragment_jobs_saved.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class SavedJobsFragment : BaseFragment(), RetryCallback, ITabSearch, JobApplyDialogFragment.Listener  {

    lateinit var viewModel: SavedJobsViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var positionApplyClicked: Int = 0

    companion object {

        fun newInstance(): SavedJobsFragment {
            return SavedJobsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs_saved

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(SavedJobsViewModel::class.java)
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

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(this, Observer {
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
                        txt_no_job_saved.visibility = View.VISIBLE
                        txt_no_results.visibility = View.GONE
                    } else {
                        txt_no_job_saved.visibility = View.GONE
                        txt_no_results.visibility = View.VISIBLE
                    }
                } else {
                    txt_no_job_saved.visibility = View.GONE
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

    private fun showJobApplyDialog(jobId: String?) {
        val dialog = JobApplyDialogFragment.newInstance(jobId!!)
        dialog.show(childFragmentManager, null)
    }

    private val listener = object : JobsViewHolder.Listener {

        override fun onApplyClick(position: Int, job: JobResponse) {
            positionApplyClicked = position
            showJobApplyDialog(job.id)
        }

        override fun onJobClick(job: JobResponse) {
            navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id, job)
        }
    }

    override fun search(searchQuery: String?) {
        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    override fun onSuccessJobApply() {
        viewModel.fetchData(viewLifecycleOwner, null)
    }
}