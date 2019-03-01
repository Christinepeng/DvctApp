package com.divercity.android.features.jobs.jobposting.jobtype

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.jobtype.JobTypeResponse
import kotlinx.android.synthetic.main.fragment_job_type.*
import kotlinx.android.synthetic.main.view_retry.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class JobTypeFragment : BaseFragment() {

    lateinit var viewModel: JobTypeViewModel

    @Inject
    lateinit var adapter: JobTypeAdapter

    companion object {

        const val JOBTYPE_PICKED = "jobTypePicked"

        fun newInstance(): JobTypeFragment {
            return JobTypeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobTypeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as JobTypeActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_job_type)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
        adapter.listener = listener
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.fetchJobTypesResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showRetry(false)
                    showProgressNoBk()
                }

                Status.ERROR -> {
                    showRetry(true)
                    hideProgressNoBk()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    showRetry(false)
                    hideProgressNoBk()
                    adapter.submitList(response.data)
                }
            }
        })
    }

    private fun showRetry(boolean: Boolean) {
        if (boolean) {
            include_retry.btn_retry.setOnClickListener {
                viewModel.fetchJobTypes()
            }
            include_retry.visibility = View.VISIBLE
        } else
            include_retry.visibility = View.GONE
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private val listener: JobTypeAdapter.Listener = object : JobTypeAdapter.Listener {

        override fun onJobTypeClick(jobType: JobTypeResponse) {
            val intent = Intent()
            intent.putExtra(JOBTYPE_PICKED, jobType)
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}