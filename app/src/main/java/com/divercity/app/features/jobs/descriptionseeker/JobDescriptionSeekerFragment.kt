package com.divercity.app.features.jobs.descriptionseeker

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.dialogs.JobSeekerActionsDialogFragment
import kotlinx.android.synthetic.main.fragment_job_description_seeker.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionSeekerFragment : BaseFragment(), JobSeekerActionsDialogFragment.Listener {

    lateinit var viewModel: JobDescriptionSeekerViewModel

    @Inject
    lateinit var adapter: JobDescriptionViewPagerAdapter

    var job: JobResponse? = null

    companion object {
        private const val PARAM_JOB = "paramJob"

        fun newInstance(job: JobResponse?): JobDescriptionSeekerFragment {
            val fragment = JobDescriptionSeekerFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_JOB, job)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_description_seeker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(JobDescriptionSeekerViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        job = arguments?.getParcelable(PARAM_JOB)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabs()
        setupView()
        initView()
    }

    private fun setupTabs(){
        job?.also {
            adapter.job = it
            viewpager.adapter = adapter
            tab_layout.setupWithViewPager(viewpager)
        }
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
        (activity as JobDescriptionSeekerActivity).apply {
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
                    .into(img_company)

            txt_company.text = it.attributes?.employer?.name
            txt_place.text = it.attributes?.locationDisplayName
            txt_title.text = it.attributes?.title

            include_user.apply {

                GlideApp.with(this)
                        .load(it.attributes?.recruiter?.avatarThumb)
                        .apply(RequestOptions().circleCrop())
                        .into(img)

                txt_name.text = it.attributes?.recruiter?.name
                txt_school.text = "Hardvard University"
                txt_type.text = "Tech Recruiter"
            }
        }
    }

    override fun onShareJobViaMessage() {
    }

    override fun onShareJobToGroups() {
        navigator.navigateToShareJobGroupActivity(this, job?.id)
    }

    override fun onReportJobPosting() {
    }
}