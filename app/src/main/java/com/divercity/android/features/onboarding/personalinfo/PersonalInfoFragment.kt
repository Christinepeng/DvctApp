package com.divercity.android.features.onboarding.personalinfo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.*

class PersonalInfoFragment : BaseFragment() {
    private var viewModel: PersonalInfoViewModel? = null
    override fun layoutId(): Int {
        return R.layout.fragment_sign_up_personal_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            PersonalInfoViewModel::class.java
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val userGender = viewModel?.sessionRepository?.getGender()
        val userEthnicityName = viewModel?.sessionRepository?.getEthnicityName()
        val userAge = viewModel?.sessionRepository?.getAgeRange()
        val userLocation = viewModel?.sessionRepository?.getLocation()

        if (userGender != null) {
            txt_user_gender.setText(userGender)
            txt_user_gender.setTextColor(Color.parseColor("#333241"))
        }
        if (userEthnicityName != null) {
            txt_user_ethnicity.setText(userEthnicityName)
            txt_user_ethnicity.setTextColor(Color.parseColor("#333241"))
        }
        if (userAge != null) {
            txt_user_age.setText(userAge)
            txt_user_age.setTextColor(Color.parseColor("#333241"))
        }
        if (userLocation != "null, null") {
            txt_user_location.setText(userLocation)
            txt_user_location.setTextColor(Color.parseColor("#333241"))
        }

        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents() {
        btn_previous_page.setOnClickListener{
            navigator.navigateToSelectUserTypeActivity(requireActivity())
        }

        btn_next.setOnClickListener{
            navigator.navigateToProfessionalInfoActivity(requireActivity())
        }

//        btn_upload_profile_picture.setOnClickListener{
//            navigator.navigateToProfessionalInfoActivity(requireActivity())
//        }

        btn_choose_gender.setOnClickListener{
            navigator.navigateToOnboardingGenderActivity(requireActivity())
        }

        btn_choose_ethnicity.setOnClickListener{
            navigator.navigateToOnboardingEthnicityActivity(requireActivity())
        }

        btn_choose_age.setOnClickListener{
            navigator.navigateToSelectBirthdayActivity(requireActivity())
        }

        btn_choose_location.setOnClickListener{
            navigator.navigateToOnboardingLocationActivity(requireActivity())
        }

        btn_skip.setOnClickListener{
            navigator.navigateToProfessionalInfoActivity(requireActivity())
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
        fun newInstance(): PersonalInfoFragment {
            return PersonalInfoFragment()
        }
    }
}