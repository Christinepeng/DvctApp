package com.divercity.android.features.user.addeditworkexperience

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.withtoolbar.ToolbarCompanyFragment
import com.divercity.android.features.dialogs.MonthYearPickerDialogFragment
import com.divercity.android.model.WorkExperience
import kotlinx.android.synthetic.main.fragment_add_work_experience.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 05/11/2018.
 */

class AddEditWorkExperienceFragment : BaseFragment(),
    MonthYearPickerDialogFragment.DatePickerDialogListener {

    lateinit var viewModel: AddEditWorkExperienceViewModel

    private var isSettingStartDate = false
    private var companyChosen: CompanyResponse? = null

    private var workExperience: WorkExperience? = null

    companion object {

        const val REQUEST_CODE_COMPANY = 180
        private const val PARAM_WORK_EXPERIENCE = "paramWorkExperience"

        fun newInstance(workExperience: WorkExperience?): AddEditWorkExperienceFragment {
            val fragment = AddEditWorkExperienceFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_WORK_EXPERIENCE, workExperience)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_add_work_experience

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workExperience = arguments?.getParcelable(PARAM_WORK_EXPERIENCE)
        viewModel =
            ViewModelProviders.of(
                this,
                viewModelFactory
            )[AddEditWorkExperienceViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeToLiveData()
    }

    private fun initView() {
        setupToolbar()

        btn_add_edt.setOnClickListener {
            if (checkFormIsCompleted())
                if (isEdition()) {
                    viewModel.editExperience(
                        workExperience?.id!!,
                        companyChosen?.id,
                        et_job_title.text.toString(),
                        txt_start_date.text.toString(),
                        txt_end_date.text.toString(),
                        btn_is_present.isSelected,
                        et_experience_details.text.toString()
                    )
                } else
                    viewModel.addWorkExperience(
                        companyChosen?.id!!,
                        et_job_title.text.toString(),
                        txt_start_date.text.toString(),
                        txt_end_date.text.toString(),
                        btn_is_present.isSelected,
                        et_experience_details.text.toString()
                    )
            else
                showToast(R.string.check_fields)
        }

        lay_start_date.setOnClickListener {
            isSettingStartDate = true
            showDialogDatePicker()
        }

        lay_end_date.setOnClickListener {
            isSettingStartDate = false
            showDialogDatePicker()
        }

        lay_employeer.setOnClickListener {
            navigator.navigateToToolbarCompanyActivityForResult(this, REQUEST_CODE_COMPANY)
        }

        btn_is_present.setOnClickListener {
            btn_is_present.isSelected = !btn_is_present.isSelected
        }

        if (isEdition()) {
            btn_add_edt.setText(R.string.update)
            setData(workExperience!!)
        } else {
            btn_add_edt.setText(R.string.add)
        }
    }

    private fun showDialogDatePicker() {
        val dialog = MonthYearPickerDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onDateSetListener(year: String?, month: String?, day: String?) {
        if (isSettingStartDate)
            txt_start_date.text = year.plus("/").plus(month)
        else
            txt_end_date.text = year.plus("/").plus(month)
    }

    private fun setData(experience: WorkExperience) {
        txt_company.text = experience.companyName
        et_job_title.setText(experience.role)
        txt_start_date.text = experience.startDate
        txt_end_date.text = experience.endDate
        btn_is_present.isSelected = experience.isPresent ?: false
        et_experience_details.setText(experience.experienceDetails)
    }

    private fun subscribeToLiveData() {
        viewModel.addEditWorkExperienceResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    finishSuccess()
                }
            }
        })
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                if (isEdition())
                    it.setTitle(R.string.edit_work_experience)
                else
                    it.setTitle(R.string.add_work_experience)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(resId: Int) {
        Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_COMPANY && resultCode == Activity.RESULT_OK) {
            val company =
                data?.extras?.getParcelable<CompanyResponse>(ToolbarCompanyFragment.COMPANY_PICKED)
            showCompanyData(company)
        }
    }

    private fun showCompanyData(company: CompanyResponse?) {
        this.companyChosen = company
        txt_company.text = company?.attributes?.name
    }

    private fun checkFormIsCompleted(): Boolean {
        return txt_company.text != "" &&
                et_job_title.text.toString() != "" &&
                txt_start_date.text != "" &&
                (txt_end_date.text != "" || btn_is_present.isSelected) &&
                et_experience_details.text.toString() != ""
    }

    private fun finishSuccess() {
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun isEdition() = workExperience != null
}