package com.divercity.app.features.jobs.description.seeker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.Status
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.app.features.dialogs.JobSeekerActionsDialogFragment
import com.divercity.app.features.dialogs.jobapply.JobApplyDialogFragment
import kotlinx.android.synthetic.main.fragment_job_description_seeker.*
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.view_job_desc.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDescriptionSeekerFragment : BaseFragment(), JobSeekerActionsDialogFragment.Listener, JobApplyDialogFragment.Listener {

    lateinit var viewModel: JobDescriptionSeekerViewModel

    @Inject
    lateinit var adapter: JobDescriptionViewPagerAdapter

    var job: JobResponse? = null

    companion object {
        private const val PARAM_JOB_ID = "paramJobId"

        fun newInstance(jobId: String): JobDescriptionSeekerFragment {
            val fragment = JobDescriptionSeekerFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_JOB_ID, jobId)
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

        val job = arguments?.getString(PARAM_JOB_ID)
        if (job != null)
            viewModel.fetchJobById(job)
        else {
            showToast(R.string.error)
            activity!!.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        setupToolbar()
    }

    private fun setupTabs(job: JobResponse?) {
        job?.also {
            adapter.job = it
            viewpager.adapter = adapter
            tab_layout.setupWithViewPager(viewpager)
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
                it.setTitle(R.string.jobs)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        btn_toolbar_more.setOnClickListener {
            showDialogMoreActions()
        }
    }

    private fun initView(job: JobResponse?) {
        job?.also {
            GlideApp.with(this)
                    .load(it.attributes?.employer?.photos?.thumb)
                    .into(inc_job_desc.img_company)

            inc_job_desc.txt_company.text = it.attributes?.employer?.name
            inc_job_desc.txt_place.text = it.attributes?.locationDisplayName
            inc_job_desc.txt_title.text = it.attributes?.title

            include_user.apply {

                GlideApp.with(this)
                        .load(it.attributes?.recruiter?.avatarThumb)
                        .apply(RequestOptions().circleCrop())
                        .into(img)

                //TODO : Remove hardcoded
                txt_name.text = it.attributes?.recruiter?.name
                txt_school.text = "Hardvard University"
                txt_type.text = "Tech Recruiter"
            }

            btn_apply.setOnClickListener {
                showJobApplyDialog()
            }
            setupSaveButton(it)
        }
    }

    private fun showJobApplyDialog() {
        val dialog = JobApplyDialogFragment.newInstance(job?.id!!)
        dialog.show(childFragmentManager, null)
    }

    private fun subscribeToLiveData() {
        viewModel.jobSaveUnsaveResponse.observe(viewLifecycleOwner, Observer { job ->
            if (job?.status == Status.LOADING)
                showProgress()
            else if (job?.status == Status.ERROR) {
                hideProgress()
                Toast.makeText(activity, job.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.fetchJobByIdResponse.observe(viewLifecycleOwner, Observer { job ->
            if (job?.status == Status.LOADING)
                showProgress()
            else if (job?.status == Status.ERROR) {
                hideProgress()
                showDialogConnectionError(job.data?.id!!)
            }
        })

        viewModel.showJobData.observe(viewLifecycleOwner, Observer { job ->
            if (job?.status == Status.SUCCESS) {
                hideProgress()
                this.job = job.data
                setupTabs(job.data)
                initView(job.data)
                root_layout.visibility = View.VISIBLE
            }
        })
    }

    private fun setupSaveButton(job: JobResponse?) {
        job?.attributes?.isBookmarkedByCurrent?.let {
            if (it) {
                btn_save.background = ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_round_blue3)
                btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                btn_save.setText(R.string.saved)
                btn_save.setOnClickListener { viewModel.removeSavedJob(job) }
            } else {
                btn_save.background = ContextCompat.getDrawable(context!!, R.drawable.bk_white_stroke_blue_rounded)
                btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.appBlue))
                btn_save.setText(R.string.save)
                btn_save.setOnClickListener { viewModel.saveJob(job) }
            }
        }
    }

    private fun showDialogConnectionError(jobId: String) {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
                getString(R.string.ups),
                getString(R.string.error_connection),
                getString(R.string.cancel),
                getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.fetchJobById(jobId)
            }

            override fun onPositiveBtnClick() {
                activity!!.finish()
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager,null)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onShareJobViaMessage() {
    }

    override fun onShareJobToGroups() {
        navigator.navigateToShareJobGroupActivity(this, job?.id)
    }

    override fun onReportJobPosting() {
    }
}