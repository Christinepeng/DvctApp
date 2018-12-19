package com.divercity.app.features.jobposting

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.data.entity.company.response.CompanyResponse
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyFragment
import com.divercity.app.features.dialogs.JobPostedDialogFragment
import com.divercity.app.features.jobposting.jobtype.JobTypeFragment
import com.divercity.app.features.jobposting.skills.JobSkillsFragment
import com.divercity.app.features.location.withtoolbar.ToolbarLocationFragment
import kotlinx.android.synthetic.main.fragment_job_posting.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import java.util.*

/**
 * Created by lucas on 05/11/2018.
 */

class JobPostingFragment : BaseFragment(), JobPostedDialogFragment.Listener {

    lateinit var viewModel: JobPostingViewModel

    var jobForEdition: JobResponse? = null

    companion object {
        private const val PARAM_JOB = "paramJob"

        const val REQUEST_CODE_LOCATION = 150
        const val REQUEST_CODE_COMPANY = 180
        const val REQUEST_CODE_JOBTYPE = 200
        const val REQUEST_CODE_SKILLS = 220

        fun newInstance(job: JobResponse?): JobPostingFragment {
            val fragment = JobPostingFragment()
            val bundle = Bundle()
            bundle.putParcelable(PARAM_JOB, job)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_posting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobPostingViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobForEdition = arguments?.getParcelable(PARAM_JOB)
        if (jobForEdition != null) {
            btn_save_create_job.setText(R.string.save)
            viewModel.setJobData(jobForEdition!!)
            setData(jobForEdition)
        } else {
            btn_save_create_job.setText(R.string.create)
            enableSaveCreateButton(false)
        }

        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.jobResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    showDialogJobPosted()
                }
            }
        })
    }

    private fun setupToolbar() {
        (activity as JobPostingActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                if(jobForEdition == null)
                    it.setTitle(R.string.create_job_posting)
                else
                    it.setTitle(R.string.edit_job_posting)

                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setData(job: JobResponse?) {
        job?.attributes?.apply {
            et_job_title.setText(title)
            txt_employeer.text = employer?.name
            txt_location.text = locationDisplayName
            txt_skills.text = skillsTag.toString().substring(1, skillsTag.toString().length - 1)
            txt_job_type.text = "Need data"
            et_job_description.setText(description)
        }
        checkFormIsCompleted()
    }

    private fun setupView() {

        lay_location.setOnClickListener {
            navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION)
        }

        lay_employeer.setOnClickListener {
            navigator.navigateToToolbarCompanyActivityForResult(this, REQUEST_CODE_COMPANY)
        }

        lay_job_type.setOnClickListener {
            navigator.navigateToJobTypeActivityForResult(this, REQUEST_CODE_JOBTYPE)
        }

        lay_skills.setOnClickListener {
            navigator.navigateToJobSkillsActivityForResult(
                    this,
                    REQUEST_CODE_SKILLS,
                    viewModel.skillList
            )
        }

        et_job_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }

        })

        et_job_description.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOCATION && resultCode == Activity.RESULT_OK) {
            val location =
                    data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
            setLocation(location)
        } else if (requestCode == REQUEST_CODE_COMPANY && resultCode == Activity.RESULT_OK) {
            val company =
                    data?.extras?.getParcelable<CompanyResponse>(ToolbarCompanyFragment.COMPANY_PICKED)
            setCompany(company)
        } else if (requestCode == REQUEST_CODE_JOBTYPE && resultCode == Activity.RESULT_OK) {
            val jobType =
                    data?.extras?.getParcelable<JobTypeResponse>(JobTypeFragment.JOBTYPE_PICKED)
            setJobType(jobType)
        } else if (requestCode == REQUEST_CODE_SKILLS && resultCode == Activity.RESULT_OK) {
            val skillList =
                    data?.extras?.getParcelableArrayList<SkillResponse>(JobSkillsFragment.SKILLS_TITLE)
            skillList?.let {
                setSkills(skillList)
            }
        }
        checkFormIsCompleted()
    }

    private fun setLocation(location: LocationResponse?) {
        location?.attributes?.let {
            txt_location.text = it.name.plus(", ").plus(it.countryName)
        }
    }

    private fun setCompany(company: CompanyResponse?) {
        viewModel.company = company
        company?.attributes?.let {
            txt_employeer.text = it.name
        }
    }

    private fun setJobType(jobType: JobTypeResponse?) {
        viewModel.jobType = jobType
        jobType?.attributes?.let {
            txt_job_type.text = it.name
        }
    }

    private fun setSkills(skillsList: ArrayList<SkillResponse>) {
        viewModel.skillList = skillsList
        txt_skills.text = skillsList.toString().substring(1, skillsList.toString().length - 1)
    }

    private fun enableSaveCreateButton(boolean: Boolean) {
        btn_save_create_job.isEnabled = boolean
        if (boolean) {
            btn_save_create_job.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
            btn_save_create_job.setOnClickListener {
                if (jobForEdition == null) {
                    viewModel.postJob(
                            et_job_title.text.toString(),
                            et_job_description.text.toString(),
                            viewModel.company!!.id!!,
                            viewModel.jobType!!.id!!,
                            txt_location.text.toString(),
                            txt_skills.text.split(",")
                    )
                } else {
                    viewModel.editJob(
                            jobForEdition!!.id!!,
                            et_job_title.text.toString(),
                            et_job_description.text.toString(),
                            viewModel.company!!.id!!,
                            viewModel.jobType!!.id!!,
                            txt_location.text.toString(),
                            txt_skills.text.split(",")
                    )
                }
            }
        } else
            btn_save_create_job.setTextColor(
                    ContextCompat.getColor(
                            activity!!,
                            R.color.whiteDisable
                    )
            )
    }

    private fun checkFormIsCompleted() {
        if (et_job_title.text.toString() != "" &&
                txt_employeer.text != "" &&
                txt_location.text != "" &&
                et_job_description.text.toString() != "" &&
                txt_job_type.text != "" &&
                txt_skills.text != "")
            enableSaveCreateButton(true)
        else
            enableSaveCreateButton(false)
    }

    private fun showDialogJobPosted() {
        val dialog = JobPostedDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onShareToGroupsClick() {
        navigator.navigateToShareJobGroupActivity(this, viewModel.jobResponse.value?.data?.id)
    }

    override fun onShareToFriendsClick() {
        showToast("Coming soon")
    }

    override fun onBtnCloseClick() {
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}