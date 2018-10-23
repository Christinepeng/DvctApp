package com.divercity.app.features.profile.selectgender;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.databinding.FragmentSelectGenderBinding;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SelectGenderFragment extends BaseFragment {

    private static final int PROGRESS = 0;

    SelectGenderViewModel viewModel;
    FragmentSelectGenderBinding binding;

    @Inject
    Context context;

    public static SelectGenderFragment newInstance() {
        SelectGenderFragment fragment = new SelectGenderFragment();
        return fragment;
    }

    public SelectGenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectGenderBinding) getViewDataBinding();
        binding.listGender.setAdapter(new SelectGenderAdapter(getContext(), listener));
        setupHeader();
        subscribeToLiveData();
    }

    private void setupHeader(){
        binding.includeHeader.progressBar.setMax(100);
        binding.includeHeader.progressBar.setProgress(0);
        binding.includeHeader.progressBar.setProgressWithAnim(PROGRESS);
        binding.includeHeader.txtProgress.setText(PROGRESS + "%");
        binding.includeHeader.txtTitle.setText(R.string.select_your_gender);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }

    private void subscribeToLiveData() {
        viewModel.dataUpdateUser.observe(this, response -> {
            Preconditions.checkNotNull(response);
            binding.includeLoading.setResource(response);
            switch (response.status){
                case ERROR:
                    break;
                case SUCCESS:
                    break;
            }
        });
    }

    SelectGenderAdapter.GenderAdapterListener listener = userType -> {
        navigator.navigateToNextProfile(getActivity(), viewModel.getAccountType());
    };
}
