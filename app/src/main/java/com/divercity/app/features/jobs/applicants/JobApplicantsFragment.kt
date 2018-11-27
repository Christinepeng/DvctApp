package com.divercity.app.features.jobs.applicants

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.dialogs.JobSeekerActionsDialogFragment
import com.divercity.app.features.dialogs.jobapply.JobApplyDialogFragment
import kotlinx.android.synthetic.main.fragment_job_description_poster.*
import kotlinx.android.synthetic.main.view_job_desc.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 16/11/2018.
 */

class JobApplicantsFragment : BaseFragment(), JobSeekerActionsDialogFragment.Listener, JobApplyDialogFragment.Listener {

    lateinit var viewModel: JobApplicantsViewModel

    var job: JobResponse? = null

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

    override fun layoutId(): Int = R.layout.fragment_job_description_poster

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(JobApplicantsViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        job = arguments?.getParcelable(PARAM_JOB)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        initView()
    }

    private fun setupView(){
        btn_toolbar_more.setOnClickListener {
            showDialogMoreActions()
        }
    }

    private fun showDialogMoreActions() {
        val dialog = JobSeekerActionsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
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

    private fun initView() {
        job?.also {
            GlideApp.with(this)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(inc_job_desc.img_company)

            inc_job_desc.txt_company.text = it.attributes?.employer?.name
            inc_job_desc.txt_place.text = it.attributes?.locationDisplayName
            inc_job_desc.txt_title.text = it.attributes?.title

            btn_view_applicants.setOnClickListener {
                showJobApplyDialog()
            }
        }
    }

    private fun showJobApplyDialog() {
        val dialog = JobApplyDialogFragment.newInstance(job?.id!!)
        dialog.show(childFragmentManager, null)
    }

    override fun onShareJobViaMessage() {
    }

    override fun onShareJobToGroups() {
        navigator.navigateToShareJobGroupActivity(this, job?.id)
    }

    override fun onReportJobPosting() {
    }
}