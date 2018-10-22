package com.divercity.app.features.profile.selectusertype;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentUserTypeBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SelectUserTypeFragment extends BaseFragment {

    SelectUserTypeViewModel viewModel;
    FragmentUserTypeBinding binding;

    @Inject
    Context context;

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
        return R.layout.fragment_user_type;
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

    public static SelectUserTypeFragment newInstance() {
        return new SelectUserTypeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentUserTypeBinding) getViewDataBinding();
        binding.listUserTypes.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        binding.listUserTypes.setAdapter(new SelectUserTypeAdapter(listener));
    }

    SelectUserTypeAdapter.UserTypeAdapterListener listener = new SelectUserTypeAdapter.UserTypeAdapterListener() {
        @Override
        public void onUserTypeClick(UserType userType) {

        }
    };
}
