package com.divercity.android.features.user.editpersonal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.android.features.company.selectcompany.withtoolbar.ToolbarCompanyFragment
import com.divercity.android.features.dialogs.ratecompany.RateCompanyDiversityDialogFragment
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.android.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_personal_settings.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import kotlinx.android.synthetic.main.view_user_personal_details.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class PersonalSettingsFragment : BaseFragment() {

    private lateinit var viewModel: PersonalSettingsViewModel

    companion object {

        const val REQUEST_CODE_RESUME = 3
        const val REQUEST_CODE_ETHNICITY = 100
        const val REQUEST_CODE_GENDER = 150
        const val REQUEST_CODE_AGE_RANGE = 180
        const val REQUEST_CODE_LOCATION = 200
        const val REQUEST_CODE_SCHOOL = 220
        const val REQUEST_CODE_OCCUPATION = 240
        const val REQUEST_CODE_COMPANY = 300

        fun newInstance(): PersonalSettingsFragment {
            return PersonalSettingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_personal_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[PersonalSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as PersonalSettingsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.personal_settings)
            }
        }
    }

    private fun setupView() {
        lay_personal.lbl_personal.visibility = View.GONE
        lay_personal.btn_edit_personal.visibility = View.GONE
        lay_personal.txt_resume.setHint(R.string.upload_your_resume)

//        ProfileUtils.setupPersonalLayout(viewModel.getUserType(),context!!, lay_personal)

        lay_personal.lay_resume.setOnClickListener {
            openDocSelector()
        }

        lay_personal.lay_ethnicity.setOnClickListener {
            navigator.navigateToToolbarEthnicityActivityForResult(this, REQUEST_CODE_ETHNICITY)
        }

        lay_personal.lay_gender.setOnClickListener {
            navigator.navigateToToolbarGenderActivityForResult(this, REQUEST_CODE_GENDER)
        }

        lay_personal.lay_age_range.setOnClickListener {
            navigator.navigateToToolbarAgeActivityForResult(this, REQUEST_CODE_AGE_RANGE)
        }

        lay_personal.lay_location.setOnClickListener {
            navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION)
        }

        lay_personal.lay_school.setOnClickListener {

        }

        lay_personal.lay_company.setOnClickListener {

        }

        lay_personal.txt_view_interests.setHint(R.string.edit_interests)
        lay_personal.lay_interests.setOnClickListener {
            navigator.navigateToInterestsActivity(this, true)
        }

        lay_personal.lay_companies.setOnClickListener {
            navigator.navigateToMyCompanies(requireActivity())
        }

        lay_personal.lay_groups.setOnClickListener {
            navigator.navigateToMyGroups(this)
        }

        lay_personal.lay_company.setOnClickListener {
            navigator.navigateToToolbarCompanyActivityForResult(this, REQUEST_CODE_COMPANY)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    setData(response.data)
                }
            }
        })

        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            setData(it)
        })

        viewModel.uploadDocumentResponse.observe(viewLifecycleOwner, Observer { document ->
            when (document?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(document.message ?: "Error")
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast(R.string.file_upload_success)
                }
            }
        })

        viewModel.showDiversityRateDialog.observe(viewLifecycleOwner, Observer {
            showRateCompanyDialog(it)
        })
    }

    private fun showRateCompanyDialog(companyId: String) {
        val dialog = RateCompanyDiversityDialogFragment.newInstance(companyId)
        dialog.listener = object : RateCompanyDiversityDialogFragment.Listener {

            override fun onCompanyRated() {
                showToast("Company Rated Successfully")
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun openDocSelector() {
        val mimeTypes = arrayOf(
            "application/pdf"
        )
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, PersonalSettingsFragment.REQUEST_CODE_RESUME)
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        if (fileUri != null) {
            viewModel.checkDocumentAndUploadIt(fileUri)
        } else
            showToast(R.string.select_valid_file)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun setData(user: User?) {
        user?.let {
            lay_personal.txt_ethnicity.text = it.ethnicity
            lay_personal.txt_gender.text = it.gender
            lay_personal.txt_age_range.text = it.ageRange
            lay_personal.txt_subtitle2.text = it.schoolName
            lay_personal.txt_occupation.text = it.occupation
            lay_personal.txt_location.text = it.fullLocation()
            lay_personal.txt_company.text = it.companyName
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ETHNICITY -> {
                    val ethnicity =
                        data?.extras?.getString(ToolbarEthnicityFragment.ETHNICITY_PICKED)
                    viewModel.updateEthnicity(ethnicity)
                }
                REQUEST_CODE_GENDER -> {
                    val gender = data?.extras?.getString(ToolbarGenderFragment.GENDER_PICKED)
                    viewModel.updateGender(gender)
                }
                REQUEST_CODE_AGE_RANGE -> {
                    val ageRange = data?.extras?.getString(ToolbarAgeFragment.AGE_RANGE_PICKED)
                    ageRange?.apply {
                        viewModel.updateAgeRange(this)
                    }
                }
                REQUEST_CODE_LOCATION -> {
                    val location =
                        data?.extras?.getParcelable<LocationResponse>(ToolbarLocationFragment.LOCATION_PICKED)
                    location?.apply {
                        viewModel.updateLocation(location)
                    }
                }
                REQUEST_CODE_RESUME -> {
                    handleDocSelectorActivityResult(data)
                }
                REQUEST_CODE_COMPANY -> {
                    val company =
                        data?.extras?.getParcelable<CompanyResponse>(ToolbarCompanyFragment.COMPANY_PICKED)
                    company?.id.apply {
                        viewModel.updateCompany(this)
                    }
                }
            }
        }
    }
}