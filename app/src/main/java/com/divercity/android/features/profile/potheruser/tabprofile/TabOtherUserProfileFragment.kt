package com.divercity.android.features.profile.potheruser.tabprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.lujun.androidtagview.TagView
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.features.profile.potheruser.OtherUserProfileViewModel
import kotlinx.android.synthetic.main.fragment_tab_profile.*
import kotlinx.android.synthetic.main.view_user_personal_details.*
import kotlinx.android.synthetic.main.view_user_personal_details.view.*

/**
 * Created by lucas on 16/11/2018.
 */

class TabOtherUserProfileFragment : BaseFragment(), RecentDocsDialogFragment.Listener {

    private lateinit var viewModel: TabOtherUserProfileViewModel

    private lateinit var sharedViewModel: OtherUserProfileViewModel

    companion object {

        fun newInstance(): TabOtherUserProfileFragment {
            return TabOtherUserProfileFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(TabOtherUserProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(OtherUserProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {
        btn_edit_experience.visibility = View.GONE
        btn_edit_education.visibility = View.GONE
        btn_edit_skills.visibility = View.GONE
        lay_personal.btn_edit_personal.visibility = View.GONE

        lay_skills.visibility = View.GONE
        lay_education.visibility = View.GONE
        lay_experience.visibility = View.GONE
        lay_interests.visibility = View.GONE
        lay_companies.visibility = View.GONE
        lay_groups.visibility = View.GONE

        txt_add_skills.visibility = View.GONE
        txt_add_experience.visibility = View.GONE
        txt_add_education.visibility = View.GONE


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

    }

    private fun setData(user: UserResponse?) {
//        ProfileUtils.setupProfileLayout(
//            user?.userAttributes?.accountType,
//            context!!,
//            lay_personal,
//            lay_skills,
//            lay_experience,
//            lay_education,
//            lay_interests
//        )

//        viewModel.fetchInterests(user)

        lay_personal.lay_resume.setOnClickListener {
//            showUploadedResumesDialog()
        }

        lay_personal.txt_ethnicity.text = user?.userAttributes?.ethnicity
        lay_personal.txt_gender.text = user?.userAttributes?.gender
        lay_personal.txt_age_range.text = user?.userAttributes?.ageRange
        lay_personal.txt_subtitle2.text = user?.userAttributes?.schoolName
        lay_personal.txt_occupation.text = user?.userAttributes?.occupation
        lay_personal.txt_location.text =
            user?.userAttributes?.city.plus(", ").plus(user?.userAttributes?.country)

        if (user?.userAttributes?.skills.isNullOrEmpty()) {
            lay_skills.visibility = View.GONE
        } else {
            lay_skills.visibility = View.VISIBLE
            tagview_skills.tags = user?.userAttributes?.skills
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

        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
            setData(it)
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
}