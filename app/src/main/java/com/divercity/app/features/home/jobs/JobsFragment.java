package com.divercity.app.features.home.jobs;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.databinding.FragmentJobsBinding;
import com.divercity.app.features.home.HomeActivity;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsFragment extends BaseFragment {

    private JobsViewModel viewModel;
    private FragmentJobsBinding binding;

    @Inject
    JobsViewPagerAdapter adapter;

    public JobsFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jobs;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(JobsViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentJobsBinding) getViewDataBinding();
        setupToolbar();
        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private void setupToolbar() {
        binding.includeToolbar.title.setText(getResources().getString(R.string.divercity));
        ((HomeActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_jobs, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
