package com.divercity.android.features.onboarding.professionalInfo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.db.converter.CompanyTypeConverter.companyToString
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.onboarding.uploadresume.UploadResumeFragment
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.btn_next
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.btn_previous_page
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.btn_skip
import kotlinx.android.synthetic.main.fragment_sign_up_professional_info.*

class ProfessionalInfoFragment : BaseFragment() {
    private var viewModel: ProfessionalInfoViewModel? = null
    override fun layoutId(): Int {
        return R.layout.fragment_sign_up_professional_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            ProfessionalInfoViewModel::class.java
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel?.sessionRepository?.getAccountType() == "student") {
//            img_job_title
            txt_job_title.setText("Major")
            select_job_title.setText("  Eg.Computer Science")
//            img_company
            txt_company.setText("School")
            select_company_name.setText("  Eg.Yale University")

            val userMajor = viewModel?.sessionRepository?.getStudentMajor()
            val userSchool = viewModel?.sessionRepository?.getSchoolName()
            if (userMajor != null) {
                if (!userMajor.isEmpty()) {
                    select_job_title.setText("  " + userMajor)
                    select_job_title.setTextColor(Color.parseColor("#333241"))
                }
            }
            if (userSchool != null) {
                select_company_name.setText("  " + userSchool)
                select_company_name.setTextColor(Color.parseColor("#333241"))
            }

        } else {
            val userOccupation = viewModel?.sessionRepository?.getOccupation()
            val userCompanyName = viewModel?.sessionRepository?.getCompanyName()
            if (userOccupation != null) {
                select_job_title.setText("  " + userOccupation)
                select_job_title.setTextColor(Color.parseColor("#333241"))
            }
            if (userCompanyName != null) {
                select_company_name.setText("  " + userCompanyName)
                select_company_name.setTextColor(Color.parseColor("#333241"))
            }
        }

        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents() {
        btn_previous_page.setOnClickListener{
            navigator.navigateToPersonalInfoActivity(requireActivity())
        }

        btn_next.setOnClickListener{
            navigator.navigateToSelectInterestsActivity(requireActivity())
        }

        btn_upload_resume.setOnClickListener{
            openDocSelector()
        }

        select_job_title.setOnClickListener{
            if (viewModel?.sessionRepository?.getAccountType() == "student") {
                navigator.navigateToSelectMajor(this, 0)
            } else {
                navigator.navigateToSelectOccupationActivity(requireActivity())
            }
        }

        select_company_name.setOnClickListener{
            if (viewModel?.sessionRepository?.getAccountType() == "student") {
                navigator.navigateToOnboardingSchool(requireActivity())
            } else {
                navigator.navigateToSelectCompanyActivity(requireActivity())
            }
        }

        et_bio.setOnClickListener{
//            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_skip.setOnClickListener{
            navigator.navigateToSelectInterestsActivity(requireActivity())
        }
    }

    private fun subscribeToLiveData() {
        viewModel?.uploadDocumentResponse?.observe(this, Observer { document ->
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
                    navigator.navigateToPersonalInfoActivity(requireActivity())
                }
            }
        })

//        viewModel.dataUpdateUser.observe(this, response -> {
//            switch (response.getStatus()) {
//                case LOADING:
//                    showProgress();
//                    break;
//                case ERROR:
//                    hideProgress();
//                    Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
//                    break;
//                case SUCCESS:
//                    hideProgress();
//                    navigator.navigateToProfilePromptActivity(getActivity());
////                    navigator.navigateToHomeActivity(getActivity());
//                    break;
//            }
//        });
    } //    SelectUserTypeAdapter.UserTypeAdapterListener listener = userType -> {

    ////        viewModel.updateUserProfile(userType.id);
//    };
    companion object {
        fun newInstance(): ProfessionalInfoFragment {
            return ProfessionalInfoFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UploadResumeFragment.REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK) {
            handleDocSelectorActivityResult(data)
        }
    }

    private fun openDocSelector() {
        val mimeTypes = arrayOf(
            "application/pdf"
        )
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, JobApplyDialogFragment.REQUEST_CODE_DOC)
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        if (fileUri != null) {
            viewModel?.checkDocumentAndUploadIt(fileUri)
        } else
            showToast(R.string.select_valid_file)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
    }
}