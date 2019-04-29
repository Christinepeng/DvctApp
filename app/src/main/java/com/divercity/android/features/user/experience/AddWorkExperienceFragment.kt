package com.divercity.android.features.user.experience

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
import kotlinx.android.synthetic.main.fragment_add_work_experience.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 05/11/2018.
 */

class AddWorkExperienceFragment : BaseFragment(),
    MonthYearPickerDialogFragment.DatePickerDialogListener {

    lateinit var viewModel: AddWorkExperienceViewModel

    var isStartDate = false
    var companyChoosen: CompanyResponse? = null

    companion object {

        const val REQUEST_CODE_COMPANY = 180

        fun newInstance(): AddWorkExperienceFragment {
            return AddWorkExperienceFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_add_work_experience

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[AddWorkExperienceViewModel::class.java]
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
                viewModel.addWorkExperience(
                    companyChoosen?.id!!,
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
            isStartDate = true
            showDialogDatePicker()
        }

        lay_end_date.setOnClickListener {
            isStartDate = false
            showDialogDatePicker()
        }

        lay_employeer.setOnClickListener {
            navigator.navigateToToolbarCompanyActivityForResult(this, REQUEST_CODE_COMPANY)
        }

        btn_is_present.setOnClickListener {
            btn_is_present.isSelected = !btn_is_present.isSelected
        }
    }

    private fun showDialogDatePicker() {
        val dialog = MonthYearPickerDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onDateSetListener(year: String?, month: String?, day: String?) {
        if (isStartDate)
            txt_start_date.text = year.plus("/").plus(month)
        else
            txt_end_date.text = year.plus("/").plus(month)
    }

    private fun subscribeToLiveData() {
        viewModel.addWorkExperienceResponse.observe(viewLifecycleOwner, Observer { response ->
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
        this.companyChoosen = company
        txt_company.text = company?.attributes?.name
    }

    private fun checkFormIsCompleted(): Boolean {
        return companyChoosen != null &&
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
}