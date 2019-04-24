package com.divercity.android.features.profile.pcurrentuser.tabprofile

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
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsAdapter
import com.divercity.android.features.profile.experience.adapter.WorkExperienceAdapter
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
    lateinit var workExperienceAdapter : WorkExperienceAdapter

    companion object {

        const val REQUEST_CODE_NEW_EXPERIENCE = 200

        fun newInstance(): TabProfileFragment {
            return TabProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(TabProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {
//        ProfileUtils.setupProfileLayout(
//            viewModel.getUserType(),
//            context!!,
//            lay_personal,
//            lay_skills,
//            lay_experience,
//            lay_education,
//            lay_interests
//        )

        lay_personal.lay_resume.setOnClickListener {
            showUploadedResumesDialog()
        }

        workExperienceAdapter.listener = {

        }
        list_experience.adapter = workExperienceAdapter

//        list_interest.layoutManager = StaggeredGridLayoutManager(2, 1)
//        list_interest.adapter = interestAdapter



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
            navigator.navigateToMyCompanies(activity!!)
        }

        lay_personal.lay_groups.setOnClickListener {
            navigator.navigateToMyGroups(this)
        }

        lay_personal.lay_interests.setOnClickListener {
            navigator.navigateToInterestsActivity(this, false)
        }

        btn_edit_experience.setOnClickListener {
            navigator.navigateToAddWorkExperienceForResult(this, REQUEST_CODE_NEW_EXPERIENCE)
        }
    }

    private fun setData(user: UserResponse?) {
        viewModel.fetchWorkExperiences()

        lay_personal.txt_ethnicity.text = user?.userAttributes?.ethnicity
        lay_personal.txt_gender.text = user?.userAttributes?.gender
        lay_personal.txt_age_range.text = user?.userAttributes?.ageRange
        lay_personal.txt_subtitle2.text = user?.userAttributes?.schoolName
        lay_personal.txt_occupation.text = user?.userAttributes?.occupation
        lay_personal.txt_location.text =
            user?.userAttributes?.city.plus(", ").plus(user?.userAttributes?.country)

        lay_personal.txt_company.text = user?.userAttributes?.company?.name

        if (user?.userAttributes?.skills.isNullOrEmpty()) {
            tagview_skills.visibility = View.GONE
            txt_add_skills.visibility = View.VISIBLE
        } else {
            tagview_skills.visibility = View.VISIBLE
            txt_add_skills.visibility = View.GONE
            tagview_skills.tags = user?.userAttributes?.skills
        }

        interestAdapter.updatedSelectedInterests(user?.userAttributes?.interestIds)

        btn_edit_skills.setOnClickListener {

            if (!user?.userAttributes?.skills.isNullOrEmpty()) {
                val skills = ArrayList<String>()
                skills.addAll(user?.userAttributes?.skills!!)
                navigator.navigateToEditUserSkills(this, skills)
            } else {
                navigator.navigateToEditUserSkills(this, null)
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

        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            setData(it)
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

        viewModel.fetchWorkExperiencesResponse.observe(this, Observer { response ->
            if(response.status == Status.SUCCESS){
                if(response?.data!!.isNotEmpty())
                    txt_add_experience.visibility = View.GONE
                else
                    txt_add_experience.visibility = View.VISIBLE
                workExperienceAdapter.list = response.data
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
        if (requestCode == REQUEST_CODE_NEW_EXPERIENCE && Activity.RESULT_OK == resultCode) {
            viewModel.fetchWorkExperiences()
        }
    }
}