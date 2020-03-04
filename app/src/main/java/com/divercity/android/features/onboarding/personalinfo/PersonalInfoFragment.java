package com.divercity.android.features.onboarding.personalinfo;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divercity.android.R;
import com.divercity.android.core.base.BaseFragment;
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeAdapter;
import com.divercity.android.features.onboarding.selectusertype.SelectUserTypeViewModel;

public class PersonalInfoFragment extends BaseFragment {

    private SelectUserTypeViewModel viewModel;
    private RecyclerView listUserType;
    private TextView userName;
    private String signUpUserName;

    public static PersonalInfoFragment newInstance() {
        return new PersonalInfoFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_sign_up_personal_info;
    }

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectUserTypeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        signUpUserName = viewModel.sessionRepository.getUserName();
//        userName = view.findViewById(R.id.select_user_type_user_name);
//        userName.setText(signUpUserName + "!");
//        listUserType = view.findViewById(R.id.list_user_types);
//        listUserType.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
//        listUserType.setAdapter(new SelectUserTypeAdapter(getContext(), listener));
        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
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
    }

//    SelectUserTypeAdapter.UserTypeAdapterListener listener = userType -> {
////        viewModel.updateUserProfile(userType.id);
//    };
}
