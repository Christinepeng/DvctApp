package com.divercity.app.features.jobs.description.poster

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.Status
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.dialogs.JobPosterActionsDialogFragment
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
                    .load(it.attributes?.employer?.photos?.thumb)
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
    }

    override fun onPublishUnpublishJobPosting() {
        viewModel.publishUnpublishJob(job!!)
    }

    override fun onDeleteJobPosting() {
    }
}