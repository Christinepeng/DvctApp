package com.divercity.app.features.profile.selectcountry;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSelectCountryBinding;

public class SelectCountryFragment extends BaseFragment {

    SelectCountryViewModel viewModel;
    FragmentSelectCountryBinding binding;

    public SelectCountryFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_country;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectCountryViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    public static SelectCountryFragment newInstance() {
        return new SelectCountryFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectCountryBinding) getViewDataBinding();
    }
}
