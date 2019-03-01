package com.divercity.android.features.profile.tabprofile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.agerange.withtoolbar.ToolbarAgeFragment
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.ethnicity.withtoolbar.ToolbarEthnicityFragment
import com.divercity.android.features.gender.withtoolbar.ToolbarGenderFragment
import com.divercity.android.features.location.withtoolbar.ToolbarLocationFragment
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsAdapter
import com.divercity.android.features.profile.settings.personalsettings.PersonalSettingsActivity
import kotlinx.android.synthetic.main.fragment_tab_profile.*
import javax.inject.Inject


/**
 * Created by lucas on 16/11/2018.
 */

class TabProfileFragment : BaseFragment(), RecentDocsDialogFragment.Listener  {

    private lateinit var viewModel: TabProfileViewModel

    @Inject
    lateinit var interestAdapter: SelectInterestsAdapter

    companion object {

        const val REQUEST_CODE_ETHNICITY = 100
        const val REQUEST_CODE_GENDER = 150
        const val REQUEST_CODE_AGE_RANGE = 180
        const val REQUEST_CODE_LOCATION = 200

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(TabProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        if (activity !is PersonalSettingsActivity) {
            viewModel.fetchInterests()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {

        if (activity is PersonalSettingsActivity) {
            lbl_personal.visibility = View.GONE
            showInterestsSection(false)
            showSkillSection(false)
            showEducationSection(false)
            showExperienceSection(false)

            lay_ethnicity.setOnClickListener {
                navigator.navigateToToolbarEthnicityActivityForResult(this, REQUEST_CODE_ETHNICITY)
            }

            lay_gender.setOnClickListener {
                navigator.navigateToToolbarGenderActivityForResult(this, REQUEST_CODE_GENDER)
            }

            lay_age_range.setOnClickListener {
                navigator.navigateToToolbarAgeActivityForResult(this, REQUEST_CODE_AGE_RANGE)
            }

            lay_location.setOnClickListener {
                navigator.navigateToToolbarLocationActivityForResult(this, REQUEST_CODE_LOCATION)
            }

            if (viewModel.getUserType() == getString(R.string.recruiter_id) ||
                viewModel.getUserType() == getString(R.string.hiring_manager_id)
            ) {
                lay_resume.visibility = View.GONE
                lay_school.visibility = View.GONE
                lay_age_range.visibility = View.GONE
            }
        } else if (viewModel.getUserType() == getString(R.string.student_id)) {
            showSkillSection(true)
            showExperienceSection(true)
            showEducationSection(true)
            showInterestsSection(true)
        } else if (viewModel.getUserType() == getString(R.string.entrepreneur_id)) {
            showInterestsSection(true)
            showSkillSection(false)
            showExperienceSection(false)
            showEducationSection(false)
        } else if (viewModel.getUserType() == getString(R.string.professional_id) ||
            viewModel.getUserType() == getString(R.string.job_seeker_id)
        ) {
            showSkillSection(true)
            showExperienceSection(true)
            showEducationSection(true)
            showInterestsSection(true)
            lay_school.visibility = View.GONE
        } else if (viewModel.getUserType() == getString(R.string.recruiter_id) ||
            viewModel.getUserType() == getString(R.string.hiring_manager_id)
        ) {
            lay_resume.visibility = View.GONE
            lay_school.visibility = View.GONE
            lay_age_range.visibility = View.GONE
            showInterestsSection(false)
            showSkillSection(false)
            showEducationSection(false)
            showExperienceSection(false)
        }

        txt_ethnicity.text = viewModel.getEthnicity()
        txt_gender.text = viewModel.getGender()
        txt_industry.text = viewModel.getIndustries()
        txt_age_range.text = viewModel.getAgeRange()
        txt_location.text = viewModel.getLocation()

        list_interest.layoutManager = StaggeredGridLayoutManager(2, 1)
        list_interest.adapter = interestAdapter

        swipe_refresh.setOnRefreshListener {
            txt_ethnicity.text = viewModel.getEthnicity()
            txt_gender.text = viewModel.getGender()
            txt_industry.text = viewModel.getIndustries()
            txt_age_range.text = viewModel.getAgeRange()
            txt_location.text = viewModel.getLocation()

            if (activity !is PersonalSettingsActivity)
                viewModel.fetchInterests()

            swipe_refresh.isRefreshing = false
        }

        lay_resume.setOnClickListener {
            showRecentDocsDialog()
        }
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
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
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

        viewModel.fetchInterestsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    interestAdapter.list = response.data
                }
            }
        })
    }

    private fun showSkillSection(enable: Boolean) {
        if (enable) {
            lbl_skills.visibility = View.VISIBLE
            list_skills.visibility = View.VISIBLE
        } else {
            lbl_skills.visibility = View.GONE
            list_skills.visibility = View.GONE
        }
    }

    private fun showExperienceSection(enable: Boolean) {
        if (enable) {
            lbl_experience.visibility = View.VISIBLE
            list_experience.visibility = View.VISIBLE
        } else {
            lbl_experience.visibility = View.GONE
            list_experience.visibility = View.GONE
        }
    }

    private fun showEducationSection(enable: Boolean) {
        if (enable) {
            lbl_education.visibility = View.VISIBLE
            list_education.visibility = View.VISIBLE
        } else {
            lbl_education.visibility = View.GONE
            list_education.visibility = View.GONE
        }
    }

    private fun showInterestsSection(enable: Boolean) {
        if (enable) {
            lbl_interests.visibility = View.VISIBLE
            list_interest.visibility = View.VISIBLE
        } else {
            lbl_interests.visibility = View.GONE
            list_interest.visibility = View.GONE
        }
    }

    private fun showRecentDocsDialog() {
        val dialog = RecentDocsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    private fun setData(user: UserResponse?) {
        txt_ethnicity.text = user?.userAttributes?.ethnicity
        txt_gender.text = user?.userAttributes?.gender
        txt_age_range.text = user?.userAttributes?.ageRange
        txt_location.text =
            user?.userAttributes?.city.plus(", ").plus(user?.userAttributes?.country)
    }

    override fun onDocumentClick(doc: DocumentResponse) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(doc.attributes?.document)
        startActivity(i)
//        navigator.navigateToLoadUrlActivity(this, doc.attributes?.document!!)
    }
}