package com.divercity.app.features.profile.selectgender;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSelectGenderBinding;

public class SelectGenderFragment extends BaseFragment {

    SelectGenderViewModel viewModel;
    FragmentSelectGenderBinding binding;

    public SelectGenderFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_gender;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectGenderViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    public static SelectGenderFragment newInstance() {
        return new SelectGenderFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectGenderBinding) getViewDataBinding();
    }
}
