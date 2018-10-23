package com.divercity.app.features.profile.selectcountry;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentSelectCountryBinding;

public class SelectCountryFragment extends BaseFragment {

    private static final int PROGRESS = 0;

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
        return BR.viewModel;
    }

    public static SelectCountryFragment newInstance() {
        return new SelectCountryFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectCountryBinding) getViewDataBinding();
        binding.btnNext.setOnClickListener(view1 -> {
            navigator.navigateToNextProfile(getActivity(), viewModel.getAccountType());
        });
        setupHeader();
    }

    private void setupHeader(){
        binding.includeHeader.progressBar.setMax(100);
        binding.includeHeader.progressBar.setProgress(0);
        binding.includeHeader.progressBar.setProgressWithAnim(PROGRESS);
        binding.includeHeader.txtProgress.setText(PROGRESS + "%");
        binding.includeHeader.txtTitle.setText(R.string.select_your_country);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }
}
