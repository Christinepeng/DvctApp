package com.divercity.android.features.education.addediteducation

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
import com.divercity.android.features.dialogs.YearPickerDialogFragment
import com.divercity.android.features.education.degree.SelectDegreeFragment
import com.divercity.android.features.major.withtoolbar.SelectSingleMajorFragment
import com.divercity.android.features.school.withtoolbar.SelectSingleSchoolFragment
import com.divercity.android.model.Degree
import com.divercity.android.model.Education
import com.divercity.android.model.Major
import com.divercity.android.model.School
import kotlinx.android.synthetic.main.fragment_add_education.*
import kotlinx.android.synthetic.main.fragment_add_work_experience.include_toolbar
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 05/11/2018.
 */

class AddEditEducationFragment : BaseFragment(),
    YearPickerDialogFragment.DatePickerDialogListener {

    lateinit var viewModel: AddEditEducationViewModel

    private var isSettingStartDate = false

    private var education: Education? = null
    private var school: School? = null
    private var major: Major? = null
    private var degree: Degree? = null

    companion object {

        private const val PARAM_EDUCATION = "paramEducation"

        const val REQUEST_CODE_SCHOOL = 220
        const val REQUEST_CODE_MAJOR = 240
        const val REQUEST_CODE_DEGREE = 290


        fun newInstance(group: Education?): AddEditEducationFragment {
            val fragment =
                AddEditEducationFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_EDUCATION, group)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_add_education

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        education = arguments?.getParcelable(PARAM_EDUCATION)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[AddEditEducationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeToLiveData()
    }

    private fun initView() {
        setupToolbar()

        txt_start_year.setOnClickListener {
            isSettingStartDate = true
            showDialogYearPicker()
        }

        txt_end_year.setOnClickListener {
            isSettingStartDate = false
            showDialogYearPicker()
        }

        txt_degree.setOnClickListener {
            navigator.navigateToSelectDegree(
                this,
                REQUEST_CODE_DEGREE
            )
        }

        txt_school.setOnClickListener {
            navigator.navigateToSelectSchool(
                this,
                REQUEST_CODE_SCHOOL
            )
        }

        txt_major.setOnClickListener {
            navigator.navigateToSelectMajor(
                this,
                REQUEST_CODE_MAJOR
            )
        }

        btn_add_edt.setOnClickListener {
            if (checkFormIsCompleted())
                if (isEdition())
                    viewModel.editEducation(
                        education!!.id,
                        school?.id,
                        major?.name,
                        txt_start_year.text.toString(),
                        txt_end_year.text.toString(),
                        degree?.id
                    )
                else
                    viewModel.addEducation(
                        school!!.id,
                        major?.name!!,
                        txt_start_year.text.toString(),
                        txt_end_year.text.toString(),
                        degree!!.id
                    )
            else
                showToast(getString(R.string.check_fields))
        }

        if (isEdition()) {
            btn_add_edt.setText(R.string.update)
            setData(education!!)
        } else {
            btn_add_edt.setText(R.string.add)
        }
    }

    private fun checkFormIsCompleted(): Boolean {
        return txt_school.text.toString() != "" &&
                txt_major.text.toString() != "" &&
                txt_start_year.text.toString() != "" &&
                txt_end_year.text.toString() != "" &&
                txt_degree.text.toString() != ""
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setData(education: Education) {
        txt_school.text = education.schoolName
        txt_major.text = education.major
        txt_start_year.text = education.startYear
        txt_end_year.text = education.endYear
    }

    private fun subscribeToLiveData() {
        viewModel.addEditEducationResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    it.setTitle(R.string.edit_education)
                else
                    it.setTitle(R.string.add_education)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onDateSetListener(year: String?, month: String?, day: String?) {
        if (isSettingStartDate)
            txt_start_year.text = year
        else
            txt_end_year.text = year
    }

    private fun finishSuccess() {
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun showDialogYearPicker() {
        val dialog = YearPickerDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    private fun isEdition() = education != null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SCHOOL -> {
                    school =
                        data?.extras?.getParcelable(SelectSingleSchoolFragment.SCHOOL_PICKED)
                    txt_school.text = school?.name
                }
                REQUEST_CODE_MAJOR -> {
                    major =
                        data?.extras?.getParcelable(SelectSingleMajorFragment.MAJOR_PICKED)
                    txt_major.text = major?.name
                }
                REQUEST_CODE_DEGREE -> {
                    degree =
                        data?.extras?.getParcelable(SelectDegreeFragment.DEGREE_PICKED)
                    txt_degree.text = degree?.name
                }
            }
        }
    }
}