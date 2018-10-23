package com.divercity.app.features.profile.selectmajor;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.ui.RetryCallback;
import com.divercity.app.data.Status;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.databinding.FragmentSelectCompanyBinding;
import com.divercity.app.features.profile.selectmajor.adapter.MajorAdapter;
import com.divercity.app.features.profile.selectmajor.adapter.MajorViewHolder;

import javax.inject.Inject;

import timber.log.Timber;

public class SelectMajorFragment extends BaseFragment implements RetryCallback {

    private static final int PROGRESS = 30;

    SelectMajorViewModel viewModel;
    FragmentSelectCompanyBinding binding;
    EditText edTxtSearch;

    @Inject
    public MajorAdapter majorAdapter;
    private boolean isRefreshing = false;

    public static SelectMajorFragment newInstance() {
        return new SelectMajorFragment();
    }

    public SelectMajorFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_company;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectMajorViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectCompanyBinding) getViewDataBinding();
        viewModel.fetchMajors(this, null);
        majorAdapter.setRetryCallback(this);
        majorAdapter.setListener(listener);
        binding.listCompany.setAdapter(majorAdapter);
        subscribeToLiveData();
        edTxtSearch = binding.includeSearch.edtxtSearch;

        edTxtSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String toSearch = edTxtSearch.getText().toString();
                if (toSearch.equals(""))
                    toSearch = null;
                viewModel.fetchMajors(SelectMajorFragment.this,
                        toSearch);
                subscribeToLiveData();
                return true;
            }
            return false;
        });

        binding.includeHeader.progressBar.setMax(100);
        binding.includeHeader.progressBar.setProgress(0);
        binding.includeHeader.progressBar.setProgressWithAnim(PROGRESS);
        binding.includeHeader.txtProgress.setText(PROGRESS + "%");
        binding.includeHeader.txtTitle.setText(R.string.select_your_major);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }

    private void subscribeToLiveData() {
        viewModel.companyList.observe(this, new Observer<PagedList<MajorResponse>>() {
            @Override
            public void onChanged(@Nullable PagedList<MajorResponse> interestItems) {
                Timber.e("questionList setting list: " + interestItems.size());
                majorAdapter.submitList(interestItems);
                if (majorAdapter.getCurrentList() != null)
                    Timber.e("questionList CURRENT LIST: " + majorAdapter.getCurrentList().size());
            }
        });

        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (majorAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + majorAdapter.getCurrentList().size());

                if (!isRefreshing || networkState.getStatus() == Status.ERROR || networkState.getStatus() == Status.SUCCESS)
                    majorAdapter.setNetworkState(networkState);
            }
        });

        viewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (majorAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + majorAdapter.getCurrentList().size() + " y " + networkState.getStatus().name());
                if (networkState != null) {
                    Timber.e("STATUS: " + networkState.getStatus().name());
                    if (majorAdapter.getCurrentList() != null) {
                        if (networkState.getStatus()
                                == NetworkState.LOADED.getStatus() || networkState.getStatus()
                                == Status.ERROR)
                            isRefreshing = false;
                        Timber.e("STATUS size: " + majorAdapter.getCurrentList().size());

                        boolean d = networkState.getStatus()
                                == NetworkState.LOADING.getStatus();
                        Timber.e("STATUS: " + d);
                    }

                    if (networkState.getStatus() == Status.SUCCESS &&
                            majorAdapter.getCurrentList().size() == 0) {
                        binding.txtNoResults.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtNoResults.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public void retry() {
        viewModel.retry();
    }

    private MajorViewHolder.Listener listener = major ->
        Toast.makeText(getContext(), major.getAttributes().getName(), Toast.LENGTH_SHORT).show();

}
