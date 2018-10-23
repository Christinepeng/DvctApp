package com.divercity.app.features.profile.selectbirthdate;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.databinding.FragmentSelectBirthdayBinding;
import com.divercity.app.features.dialogs.DatePickerDialogFragment;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class SelectBirthdayFragment extends BaseFragment implements DatePickerDialogFragment.DatePickerDialogListener {

    private static final int PROGRESS = 0;

    SelectBirthdayViewModel viewModel;
    FragmentSelectBirthdayBinding binding;

    @Inject
    Context context;

    public static SelectBirthdayFragment newInstance() {
        SelectBirthdayFragment fragment = new SelectBirthdayFragment();
        return fragment;
    }

    public SelectBirthdayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_birthday;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectBirthdayViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectBirthdayBinding) getViewDataBinding();
        setupHeader();
        subscribeToLiveData();
        binding.layDate.setOnClickListener(view1 -> {
            showDatePickerDialog();
        });
    }

    public void showDatePickerDialog() {
        DialogFragment f = DatePickerDialogFragment.newInstance();
        f.show(getChildFragmentManager(), null);
    }

    private void setupHeader(){
        binding.includeHeader.progressBar.setMax(100);
        binding.includeHeader.progressBar.setProgress(0);
        binding.includeHeader.progressBar.setProgressWithAnim(PROGRESS);
        binding.includeHeader.txtProgress.setText(PROGRESS + "%");
        binding.includeHeader.txtTitle.setText(R.string.select_your_birthday);

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
                    Toast.makeText(getActivity(),response.message,Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    binding.txtDate.setText(response.data.getAttributes().getBirthdate());
                    break;
            }
        });
    }

    @Override
    public void onDateSetListener(String year, String month, String day) {
        viewModel.updateUserProfile(year,month,day);
    }
}
