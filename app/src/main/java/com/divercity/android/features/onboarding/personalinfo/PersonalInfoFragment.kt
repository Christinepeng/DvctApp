package com.divercity.android.features.onboarding.personalinfo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel
import kotlinx.android.synthetic.main.fragment_sign_up_personal_info.*

class PersonalInfoFragment : BaseFragment() {
    private var viewModel: SelectUserTypeViewModel? = null
    private val listUserType: RecyclerView? = null
    private val userName: TextView? = null
    private val signUpUserName: String? = null
    override fun layoutId(): Int {
        return R.layout.fragment_sign_up_personal_info
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            SelectUserTypeViewModel::class.java
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        //        signUpUserName = viewModel.sessionRepository.getUserName();
//        userName = view.findViewById(R.id.select_user_type_user_name);
//        userName.setText(signUpUserName + "!");
//        listUserType = view.findViewById(R.id.list_user_types);
//        listUserType.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
//        listUserType.setAdapter(new SelectUserTypeAdapter(getContext(), listener));
        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents() {
        btn_next.setOnClickListener{
            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_skip.setOnClickListener{
            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_choose_gender.setOnClickListener{
            navigator.navigateToOnboardingGenderActivity(requireActivity())
        }

        btn_choose_ethnicity.setOnClickListener{
            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_choose_age.setOnClickListener{
            navigator.navigateToHomeActivity(requireActivity())
        }

        btn_choose_location.setOnClickListener{
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
        fun newInstance(): PersonalInfoFragment {
            return PersonalInfoFragment()
        }
    }
}