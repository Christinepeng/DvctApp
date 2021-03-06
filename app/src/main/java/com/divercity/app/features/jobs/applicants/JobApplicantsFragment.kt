package com.divercity.app.features.jobs.applicants

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.Status
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.jobs.applicants.adapter.JobApplicantsAdapter
import com.divercity.app.features.jobs.applicants.adapter.JobApplicantsViewHolder
import kotlinx.android.synthetic.main.fragment_job_applicants.*
import kotlinx.android.synthetic.main.view_job_desc.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobApplicantsFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: JobApplicantsViewModel

    @Inject
    lateinit var adapter: JobApplicantsAdapter

    var job: JobResponse? = null
    private var isListRefreshing = false

    companion object {
        private const val PARAM_JOB = "paramJob"

        fun newInstance(job: JobResponse?): JobApplicantsFragment {
            val fragment = JobApplicantsFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_JOB, job)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_applicants

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(JobApplicantsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        job = arguments?.getParcelable(PARAM_JOB)

        if (savedInstanceState == null)
            viewModel.fetchApplicants(job?.id!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initView()
        initList()
        subscribeToPaginatedLiveData()
    }

    private fun initList() {
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list_main.adapter = adapter

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

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.view_applicants)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedApplicantsList.observe(this, Observer {
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
                    txt_no_applicants.visibility = View.VISIBLE
                else
                    txt_no_applicants.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun initView() {
        job?.also {
            GlideApp.with(this)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(inc_job_desc.img_company)

            inc_job_desc.txt_company.text = it.attributes?.employer?.name
            inc_job_desc.txt_place.text = it.attributes?.locationDisplayName
            inc_job_desc.txt_title.text = it.attributes?.title
        }
    }

    override fun retry() {
        viewModel.retry()
    }

    private
    val listener: JobApplicantsViewHolder.Listener = object : JobApplicantsViewHolder.Listener {

    }
}