package com.divercity.android.features.jobs.detail.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.android.features.dialogs.JobSeekerActionsDialogFragment
import com.divercity.android.features.dialogs.jobapplication.JobApplicationDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import kotlinx.android.synthetic.main.fragment_job_detail.*
import kotlinx.android.synthetic.main.item_user_action.view.*
import kotlinx.android.synthetic.main.view_job_desc.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class JobDetailFragment : BaseFragment(), JobSeekerActionsDialogFragment.Listener
    ,JobApplicationDialogFragment.Listener {

    lateinit var viewModel: JobDetailViewModel

    @Inject
    lateinit var adapter: JobDetailViewPagerAdapter

    companion object {
        private const val PARAM_JOB_ID = "paramJobId"

        fun newInstance(jobId: String): JobDetailFragment {
            val fragment = JobDetailFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    enum class DataHolder {
        INSTANCE;

        private var job: JobResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.job != null
            }

            var data: JobResponse?
                get() {
                    val jobResponse = INSTANCE.job
                    INSTANCE.job = null
                    return jobResponse
                }
                set(objectList) {
                    INSTANCE.job = objectList
                }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(requireActivity(), viewModelFactory)
                .get(JobDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        setupToolbar()

        if (DataHolder.hasData()) {
            viewModel.setJob(DataHolder.data!!)
        } else if (viewModel.jobLiveData.value == null) {
            viewModel.jobId = arguments?.getString(PARAM_JOB_ID)!!
            viewModel.fetchJob()
        }
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

    private fun showDialogMoreActions() {
        val dialog = JobSeekerActionsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    private fun showJob(job: JobResponse?) {
        job?.also {

            adapter.job = it
            if (viewpager.adapter == null) {
                viewpager.adapter = adapter
                tab_layout.setupWithViewPager(viewpager)
            }

            root_layout.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.attributes?.employer?.photos?.original)
                .into(inc_job_desc.img_company)

            inc_job_desc.txt_company.text = it.attributes?.employer?.name
            inc_job_desc.txt_place.text = it.attributes?.locationDisplayName
            inc_job_desc.txt_title.text = it.attributes?.title

            include_user.apply {

                GlideApp.with(this)
                    .load(it.attributes?.recruiter?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(include_img_desc.img)

                include_img_desc.txt_name.text = it.attributes?.recruiter?.name
                include_img_desc.txt_subtitle1.text = it.attributes?.recruiter?.occupation
            }

            if (viewModel.isLoggedUserJobSeeker()) {
                showHideButtonsIfApplied(it.attributes?.isAppliedByCurrent!!)
                setupApplyButton(it)
            } else {
                btn_apply.visibility = View.GONE
            }
            setupSaveButton(it)
        }
    }

    private fun showJobApplyDialog() {
        val dialog = JobApplyDialogFragment.newInstance(viewModel.getJob())
        dialog.listener = object : JobApplyDialogFragment.Listener {

            override fun onSuccessJobApply() {
                viewModel.onJobApplied()
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun subscribeToLiveData() {
        viewModel.jobSaveUnsaveResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.fetchJobResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showDialogConnectionError()
                }

                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.jobLiveData.observe(viewLifecycleOwner, Observer { job ->
            showJob(job)
        })
    }

    private fun setupSaveButton(job: JobResponse?) {
        job?.attributes?.isBookmarkedByCurrent?.let {
            if (it) {
                btn_save.background =
                    ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_round_blue3)
                btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                btn_save.setText(R.string.saved)
                btn_save.setOnClickListener { viewModel.removeSavedJob(job) }
            } else {
                btn_save.background =
                    ContextCompat.getDrawable(context!!, R.drawable.bk_white_stroke_blue_rounded)
                btn_save.setTextColor(ContextCompat.getColor(context!!, R.color.appBlue))
                btn_save.setText(R.string.save)
                btn_save.setOnClickListener { viewModel.saveJob(job) }
            }
        }
    }

    private fun setupApplyButton(job: JobResponse?) {
        job?.attributes?.isAppliedByCurrent?.let {
            if (!it) {
                btn_apply.background =
                    ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_round_blue3)
                btn_apply.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                btn_apply.setText(R.string.apply)
                btn_apply.setOnClickListener {
                    showJobApplyDialog()
                }
            } else {
                btn_apply.background =
                    ContextCompat.getDrawable(context!!, R.drawable.bk_white_stroke_blue_rounded)
                btn_apply.setTextColor(ContextCompat.getColor(context!!, R.color.appBlue))
                btn_apply.setText(R.string.applied)
            }
        }
    }

    private fun showDialogConnectionError() {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.error_connection),
            getString(R.string.cancel),
            getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.fetchJob()
            }

            override fun onPositiveBtnClick() {
                requireActivity().finish()
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
    }

    private fun showHideButtonsIfApplied(isAppliedByCurrent: Boolean) {
        if (isAppliedByCurrent) {
            btn_apply.visibility = View.GONE
            btn_save.visibility = View.GONE
            btn_view_application.visibility = View.VISIBLE
            btn_view_application.setOnClickListener {
                showJobApplicationDialog()
            }
        } else {
            btn_apply.visibility = View.VISIBLE
            btn_save.visibility = View.VISIBLE
            btn_view_application.visibility = View.GONE
        }
    }

    private fun showJobApplicationDialog() {
        val fragment = JobApplicationDialogFragment.newInstance(viewModel.jobId)
        fragment.show(childFragmentManager, null)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(requireContext().applicationContext, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onShareJobViaMessage() {
        navigator.navigateToShareJobViaMessage(this, viewModel.jobId)
    }

    override fun onShareJobToGroups() {
        navigator.navigateToShareJobGroupActivity(this, viewModel.jobId)
    }

    override fun onReportJobPosting() {
    }

    override fun onCancelJobApplication() {
        viewModel.onCancelJobApplication()
    }
}