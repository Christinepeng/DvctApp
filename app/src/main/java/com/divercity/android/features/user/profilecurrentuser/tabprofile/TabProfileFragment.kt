package com.divercity.android.features.user.profilecurrentuser.tabprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.lujun.androidtagview.TagView
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsAdapter
import com.divercity.android.features.user.addediteducation.adapter.EducationAdapter
import com.divercity.android.features.user.addeditworkexperience.adapter.WorkExperienceAdapter
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_tab_profile.*
import kotlinx.android.synthetic.main.view_user_personal_details.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class TabProfileFragment : BaseFragment(), RecentDocsDialogFragment.Listener {

    private lateinit var viewModel: TabProfileViewModel

    @Inject
    lateinit var interestAdapter: SelectInterestsAdapter

    @Inject
    lateinit var workExperienceAdapter: WorkExperienceAdapter

    @Inject
    lateinit var educationAdapter: EducationAdapter

    companion object {

        const val REQUEST_CODE_NEW_EXPERIENCE_EDUCATION = 200

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(TabProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {

        lay_personal.lay_resume.setOnClickListener {
            showUploadedResumesDialog()
        }

        list_experience.adapter = workExperienceAdapter

        list_education.adapter = educationAdapter

        val typeface = ResourcesCompat.getFont(context!!, R.font.avenir_medium)
        tagview_skills.tagTypeface = typeface
        tagview_skills.setOnTagClickListener(object : TagView.OnTagClickListener {

            override fun onSelectedTagDrag(position: Int, text: String?) {
            }

            override fun onTagLongClick(position: Int, text: String?) {
            }

            override fun onTagClick(position: Int, text: String?) {
            }

            override fun onTagCrossClick(position: Int) {
            }
        })

        lay_personal.btn_edit_personal.setOnClickListener {
            navigator.navigateToPersonalSettingsActivity(this)
        }

        lay_personal.lay_companies.setOnClickListener {
            navigator.navigateToMyCompanies(requireActivity())
        }

        lay_personal.lay_groups.setOnClickListener {
            navigator.navigateToMyGroups(this)
        }

        lay_personal.lay_interests.setOnClickListener {
            navigator.navigateToInterestsActivity(this, false)
        }

        btn_edit_experience.setOnClickListener {
            navigator.navigateToEditExperienceEducationForResult(
                this@TabProfileFragment,
                REQUEST_CODE_NEW_EXPERIENCE_EDUCATION
            )
        }

        btn_edit_education.setOnClickListener {
            navigator.navigateToEditExperienceEducationForResult(
                this@TabProfileFragment,
                REQUEST_CODE_NEW_EXPERIENCE_EDUCATION
            )
        }
    }

    private fun setData(user: User?) {
        user?.let { usr ->
            viewModel.fetchWorkExperiences()
            viewModel.fetchEducations()

            lay_personal.txt_ethnicity.text = usr.ethnicity
            lay_personal.txt_gender.text = usr.gender
            lay_personal.txt_age_range.text = usr.ageRange
            lay_personal.txt_subtitle2.text = usr.schoolName
            lay_personal.txt_occupation.text = usr.occupation
            lay_personal.txt_location.text = usr.fullLocation()
            lay_personal.txt_company.text = usr.companyName

            if (usr.skills.isNullOrEmpty()) {
                tagview_skills.visibility = View.GONE
                txt_add_skills.visibility = View.VISIBLE
            } else {
                tagview_skills.visibility = View.VISIBLE
                txt_add_skills.visibility = View.GONE
                tagview_skills.tags = usr.skills
            }

            interestAdapter.updatedSelectedInterests(usr.interestIds)

            btn_edit_skills.setOnClickListener {

                if (!usr.skills.isNullOrEmpty()) {
                    val skills = ArrayList<String>()
                    skills.addAll(usr.skills!!)
                    navigator.navigateToEditUserSkills(this, skills)
                } else {
                    navigator.navigateToEditUserSkills(this, null)
                }
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
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

        viewModel.fetchInterestsResponse.observe(viewLifecycleOwner, Observer { response ->
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

        viewModel.fetchWorkExperiencesResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.status == Status.SUCCESS) {
                if (response?.data!!.isNotEmpty())
                    txt_add_experience.visibility = View.GONE
                else
                    txt_add_experience.visibility = View.VISIBLE
                workExperienceAdapter.list = response.data
            }
        })

        viewModel.fetchEducationsResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.status == Status.SUCCESS) {
                if (response?.data!!.isNotEmpty())
                    txt_add_education.visibility = View.GONE
                else
                    txt_add_education.visibility = View.VISIBLE
                educationAdapter.list = response.data
            }
        })
    }

    private fun showUploadedResumesDialog() {
        val dialog = RecentDocsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onDocumentClick(doc: DocumentResponse) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(doc.attributes?.document)
        startActivity(i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUEST_CODE_NEW_EXPERIENCE_EDUCATION -> {
                    viewModel.fetchWorkExperiences()
                    viewModel.fetchEducations()
                }
            }
    }
}