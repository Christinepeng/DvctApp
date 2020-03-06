package com.divercity.android.features.onboarding.professionalInfo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.db.converter.CompanyTypeConverter.companyToString
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
            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_upload_resume.setOnClickListener{
//            navigator.navigateToHomeActivity(requireActivity())
        }

        select_job_title.setOnClickListener{
            if (viewModel?.sessionRepository?.getAccountType() == "student") {
                navigator.navigateToOnboardingMajor(requireActivity())
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
            navigator.navigateToHomeActivity(requireActivity())
        }
    }

    private fun subscribeToLiveData() { //        viewModel.dataUpdateUser.observe(this, response -> {
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
}