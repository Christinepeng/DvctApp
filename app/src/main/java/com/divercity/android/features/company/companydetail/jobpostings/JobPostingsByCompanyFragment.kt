package com.divercity.android.features.company.companydetail.jobpostings

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
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.jobs.jobs.adapter.JobsAdapter
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import com.divercity.android.model.position.JobPosition
import kotlinx.android.synthetic.main.fragment_job_postings_by_company.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class JobPostingsByCompanyFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: JobPostingsByCompanyViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    private var isListRefreshing = false
    private var positionApplyClicked: Int = 0

    companion object {

        private const val PARAM_COMPANY_ID = "paramCommpanyId"

        fun newInstance(companyId: String): JobPostingsByCompanyFragment {
            val fragment = JobPostingsByCompanyFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_postings_by_company

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(JobPostingsByCompanyViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchData(arguments!!.getString(PARAM_COMPANY_ID)!!)
        initSwipeToRefresh()
        initAdapter()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.listener = object : JobsViewHolder.Listener {

            override fun onApplyClick(jobPos : JobPosition) {
                positionApplyClicked = jobPos.position
                showJobApplyDialog(jobPos.job.id)
            }

            override fun onJobClick(jobPos : JobPosition) {
                navigator.navigateToJobDetail(requireActivity(), jobPos.job.id, jobPos.job)
            }
        }
        list.adapter = adapter
    }

    fun fetchLiveData() {
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
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
                    adapter.currentList?.get(positionApplyClicked)
                        ?.attributes?.isBookmarkedByCurrent =
                        job.data?.attributes?.isBookmarkedByCurrent
                    adapter.notifyItemChanged(positionApplyClicked)
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

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_jobs.visibility = View.VISIBLE
                else
                    txt_no_jobs.visibility = View.GONE

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
}