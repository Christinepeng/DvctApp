package com.divercity.app.features.profile.selectusertype;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.databinding.FragmentSelectUserTypeBinding;
import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SelectUserTypeFragment extends BaseFragment {

    public static final String PARAM_COMPLETE_PROFILE = "param_complete_profile";

    SelectUserTypeViewModel viewModel;
    FragmentSelectUserTypeBinding binding;

    @Inject
    Context context;

    public static SelectUserTypeFragment newInstance(boolean completeProfile) {
        SelectUserTypeFragment fragment = new SelectUserTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(PARAM_COMPLETE_PROFILE, completeProfile);
        fragment.setArguments(bundle);
        return fragment;
    }

    public SelectUserTypeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_user_type;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectUserTypeViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectUserTypeBinding) getViewDataBinding();
        binding.listUserTypes.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        binding.listUserTypes.setAdapter(new SelectUserTypeAdapter(getContext(), listener));
        subscribeToLiveData();
    }

    private void subscribeToLiveData() {
        viewModel.dataUpdateUser.observe(this, response -> {
            Preconditions.checkNotNull(response);
            binding.includeLoading.setResource(response);
            switch (response.status){
                case ERROR:
                    Toast.makeText(getActivity(),response.message,Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    if(getArguments().getBoolean(PARAM_COMPLETE_PROFILE))
                        navigator.navigateToNextProfile(getActivity(), response.data.getAttributes().getAccountType());
                    else
                        navigator.navigateToHome(getActivity());
                    break;
            }
        });
    }

    SelectUserTypeAdapter.UserTypeAdapterListener listener = userType -> {
        viewModel.updateUserProfile(userType.id);
    };
}
