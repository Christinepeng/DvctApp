package com.divercity.android.features.jobs.jobs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogFragment
import com.divercity.android.features.jobs.jobs.adapter.JobsAdapter
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import com.divercity.android.features.search.ITabSearch
import com.divercity.android.model.position.JobPosition
import kotlinx.android.synthetic.main.fragment_jobs_list.*
import javax.inject.Inject


/**
 * Created by lucas on 25/10/2018.
 */

class JobsListFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: JobsListViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var lastJobPositionTap = 0

    companion object {

        const val REQUEST_CODE_JOB = 201

        fun newInstance(): JobsListFragment {
            return JobsListFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_jobs_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(JobsListViewModel::class.java)
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
        adapter.listener = object : JobsViewHolder.Listener {

            override fun onApplyClick(jobPos: JobPosition) {
                showJobApplyDialog(jobPos)
            }

            override fun onJobClick(jobPos: JobPosition) {
                lastJobPositionTap = jobPos.position
                navigator.navigateToJobDetailForResult(
                    this@JobsListFragment,
                    jobPos.job,
                    REQUEST_CODE_JOB
                )
            }
        }
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->

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

    private fun showJobApplyDialog(jobPos: JobPosition) {
        val dialog = JobApplyDialogFragment.newInstance(jobPos.job)
        dialog.listener = object : JobApplyDialogFragment.Listener {

            override fun onSuccessJobApply() {
                adapter.notifyItemChanged(jobPos.position)
            }
        }
        dialog.show(childFragmentManager, null)
    }

    override fun search(searchQuery: String?) {
        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    fun onOpenFilterMenu() {
//        showJobSearchFilterDialog()
    }

    private fun showJobSearchFilterDialog() {
        val dialog = JobSearchFilterDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_JOB) {
            adapter.notifyItemChanged(lastJobPositionTap)
        }
    }
}