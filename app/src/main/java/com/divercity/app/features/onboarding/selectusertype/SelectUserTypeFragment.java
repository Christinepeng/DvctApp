package com.divercity.app.features.onboarding.selectusertype;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;

public class SelectUserTypeFragment extends BaseFragment {

    SelectUserTypeViewModel viewModel;

    RecyclerView listUserType;

    public static SelectUserTypeFragment newInstance() {
        return new SelectUserTypeFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_select_user_type;
    }

    public SelectUserTypeFragment() {
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
        listUserType = view.findViewById(R.id.list_user_types);
        listUserType.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        listUserType.setAdapter(new SelectUserTypeAdapter(getContext(), listener));
        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
        viewModel.dataUpdateUser.observe(this, response -> {
            switch (response.status) {
                case LOADING:
                    showProgress();
                    break;
                case ERROR:
                    hideProgress();
                    Toast.makeText(getActivity(), response.message, Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    hideProgress();
                    navigator.navigateProfilePromptActivity(getActivity());
                    break;
            }
        });
    }

    SelectUserTypeAdapter.UserTypeAdapterListener listener = userType -> {
        viewModel.updateUserProfile(userType.id);
    };
}
