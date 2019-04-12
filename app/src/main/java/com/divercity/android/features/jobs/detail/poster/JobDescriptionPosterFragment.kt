package com.divercity.android.features.jobs.detail.poster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.android.features.dialogs.JobPosterActionsDialogFragment
import com.divercity.android.features.jobs.mypostings.MyJobsPostingsFragment
import kotlinx.android.synthetic.main.fragment_job_description_poster.*
import kotlinx.android.synthetic.main.view_job_desc.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionPosterFragment : BaseFragment(), JobPosterActionsDialogFragment.Listener {

    lateinit var viewModel: JobDescriptionPosterViewModel

    @Inject
    lateinit var adapter: JobDescriptionPosterViewPagerAdapter

    var job: JobResponse? = null

    companion object {
        private const val PARAM_JOB = "paramJob"

        fun newInstance(job: JobResponse?): JobDescriptionPosterFragment {
            val fragment = JobDescriptionPosterFragment()
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
            ViewModelProviders.of(this, viewModelFactory).get(JobDescriptionPosterViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        job = arguments?.getParcelable(PARAM_JOB)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabs()
        setupView()
        initView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {

        viewModel.publishUnpublishJobResponse.observe(viewLifecycleOwner, Observer { job ->
            when (job?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, job.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    this.job = job.data
                    hideProgress()
                    job.data?.attributes?.publishable?.let {
                        if(it)
                            Toast.makeText(activity, R.string.publish_success, Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(activity, R.string.unpublish_success, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.deleteJobResponse.observe(viewLifecycleOwner, Observer { job ->
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
                    activity?.apply {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
        })
    }

    private fun setupTabs() {
        job?.also {
            adapter.job = it
            viewpager.adapter = adapter
            tab_layout.setupWithViewPager(viewpager)
        }
    }

    private fun setupView() {
        btn_toolbar_more.setOnClickListener {
            showDialogMoreActions()
        }
    }

    private fun showDialogMoreActions() {
        val dialog = JobPosterActionsDialogFragment.newInstance(job!!)
        dialog.show(childFragmentManager, null)
    }

    private fun setupToolbar() {
        (activity as JobDescriptionPosterActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.jobs)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initView() {
        job?.also {
            GlideApp.with(this)
                    .load(it.attributes?.employer?.photos?.original)
                    .into(inc_job_desc.img_company)

            inc_job_desc.txt_company.text = it.attributes?.employer?.name
            inc_job_desc.txt_place.text = it.attributes?.locationDisplayName
            inc_job_desc.txt_title.text = it.attributes?.title

            btn_view_applicants.setOnClickListener {
                navigator.navigateToJobApplicantsActivity(this, job!!)
            }
        }
    }

    override fun onEditJobPosting() {
        navigator.navigateToJobPostingForResultActivity(this, MyJobsPostingsFragment.REQUEST_CODE_UPDATE, job)
    }

    override fun onPublishUnpublishJobPosting() {
        viewModel.publishUnpublishJob(job!!)
    }

    override fun onDeleteJobPosting() {
        showDeleteDialogConfirm()
    }

    private fun showDeleteDialogConfirm() {

        val dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.delete_job),
            getString(R.string.delete_job_confirmation),
            getString(R.string.no),
            getString(R.string.yes)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.deleteJob(job!!.id!!)
            }

            override fun onPositiveBtnClick() {
            }
        })
        dialog.show(childFragmentManager, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == MyJobsPostingsFragment.REQUEST_CODE_UPDATE){
                activity?.apply {
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}