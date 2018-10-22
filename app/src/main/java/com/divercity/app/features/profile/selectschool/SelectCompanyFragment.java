package com.divercity.app.features.profile.selectschool;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSelectCompanyBinding;

public class SelectCompanyFragment extends BaseFragment {

    SelectCompanyViewModel viewModel;
    FragmentSelectCompanyBinding binding;

    public SelectCompanyFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_company;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectCompanyViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    public static SelectCompanyFragment newInstance() {
        return new SelectCompanyFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectCompanyBinding) getViewDataBinding();
    }

}
