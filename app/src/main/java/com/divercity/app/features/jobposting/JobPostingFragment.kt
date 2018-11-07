package com.divercity.app.features.jobposting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.company.CompanyResponse
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.company.withtoolbar.ToolbarCompanyFragment
import com.divercity.app.features.jobposting.jobtype.JobTypeFragment
import com.divercity.app.features.location.withtoolbar.ToolbarLocationFragment
import kotlinx.android.synthetic.main.fragment_job_posting.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 05/11/2018.
 */

class JobPostingFragment : BaseFragment() {

    companion object {

        const val REQUEST_CODE_LOCATION = 150
        const val REQUEST_CODE_COMPANY = 180
        const val REQUEST_CODE_JOBTYPE = 200

        fun newInstance() = JobPostingFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_job_posting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        (activity as JobPostingActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_job_posting)
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.ic_close_24dp)
            }
        }
    }

    private fun setupView() {
        enableCreateButton(false)

        lay_location.setOnClickListener {
            navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION, "")
        }

        lay_employeer.setOnClickListener {
            navigator.navigateToToolbarCompanyActivityForResult(this, REQUEST_CODE_COMPANY)
        }

        lay_job_type.setOnClickListener {
            navigator.navigateToJobTypeActivityForResult(this, REQUEST_CODE_JOBTYPE)
        }

        et_job_title.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkFormIsCompleted()
            }

        })

        et_job_description.addTextChangedListener(object : TextWatcher{
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
            val location = data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
            setLocation(location)
        } else if (requestCode == REQUEST_CODE_COMPANY && resultCode == Activity.RESULT_OK) {
            val company = data?.extras?.getParcelable<CompanyResponse>(ToolbarCompanyFragment.COMPANY_PICKED)
            setCompany(company)
        } else if (requestCode == REQUEST_CODE_JOBTYPE && resultCode == Activity.RESULT_OK) {
            val jobType = data?.extras?.getParcelable<JobTypeResponse>(JobTypeFragment.JOBTYPE_PICKED)
            setJobType(jobType)
        }
        checkFormIsCompleted()
    }

    private fun setLocation(location: LocationResponse?) {
        location?.attributes?.let {
            txt_location.text = it.name.plus(", ").plus(it.countryName)
        }
    }

    private fun setCompany(company: CompanyResponse?) {
        company?.attributes?.let {
            txt_employeer.text = it.name
        }
    }

    private fun setJobType(jobType: JobTypeResponse?) {
        jobType?.attributes?.let {
            txt_job_type.text = it.name
        }
    }

    private fun enableCreateButton(boolean: Boolean) {
        btn_create_job.isEnabled = boolean
        if (boolean)
            btn_create_job.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
        else
            btn_create_job.setTextColor(ContextCompat.getColor(activity!!, R.color.whiteDisable))
    }

    private fun checkFormIsCompleted() {
        if (et_job_title.text.toString() != "" &&
                txt_employeer.text != "" &&
                txt_location.text != "" &&
                et_job_description.text.toString() != "" &&
                txt_job_type.text != "")
            enableCreateButton(true)
        else
            enableCreateButton(false)
    }
}