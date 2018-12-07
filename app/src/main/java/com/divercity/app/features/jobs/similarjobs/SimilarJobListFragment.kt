package com.divercity.app.features.jobs.similarjobs

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
import com.divercity.app.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.app.features.jobs.jobs.adapter.JobsAdapter
import com.divercity.app.features.jobs.jobs.adapter.JobsViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class SimilarJobListFragment : BaseFragment(), RetryCallback, JobApplyDialogFragment.Listener {

    lateinit var viewModel: SimilarJobListViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var positionApplyClicked: Int = 0

    private lateinit var jobId: String

    companion object {
        private const val PARAM_JOB_ID = "paramJobId"

        fun newInstance(jobId: String): SimilarJobListFragment {
            val fragment = SimilarJobListFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(SimilarJobListViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        jobId = arguments!!.getString(PARAM_JOB_ID, "")
        if (savedInstanceState == null)
            viewModel.fetchSimilarJobs(jobId)
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
        viewModel.jobSaveUnsaveResponse.observe(this, Observer { job ->
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
                    adapter.currentList?.get(positionApplyClicked)?.attributes?.isBookmarkedByCurrent =
                            job.data?.attributes?.isBookmarkedByCurrent
                    adapter.notifyItemChanged(positionApplyClicked)
                }
            }
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

    private val listener: JobsViewHolder.Listener = object : JobsViewHolder.Listener {

        override fun onApplyClick(position: Int, job: JobResponse) {
            positionApplyClicked = position
            showJobApplyDialog(job.id)
        }

        override fun onJobClick(job: JobResponse) {
            navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id)
        }
    }

    private fun showJobApplyDialog(jobId: String?) {
        val dialog = JobApplyDialogFragment.newInstance(jobId!!)
        dialog.show(childFragmentManager, null)
    }

    override fun onSuccessJobApply() {
        viewModel.refresh()
    }
}