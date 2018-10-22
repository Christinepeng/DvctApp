package com.divercity.app.features.profile.selectethnicity;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSelectEthnicityBinding;

public class SelectEthnicityFragment extends BaseFragment {

    SelectEthnicityViewModel viewModel;
    FragmentSelectEthnicityBinding binding;

    public SelectEthnicityFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_ethnicity;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectEthnicityViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    public static SelectEthnicityFragment newInstance() {
        return new SelectEthnicityFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectEthnicityBinding) getViewDataBinding();
    }
}
