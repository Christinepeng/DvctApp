package com.divercity.app.features.profile.selectschool;


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
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.databinding.FragmentSelectCompanyBinding;
import com.divercity.app.features.profile.selectschool.adapter.CompanyAdapter;
import com.divercity.app.features.profile.selectschool.adapter.CompanyViewHolder;

import javax.inject.Inject;

import timber.log.Timber;

public class SelectCompanyFragment extends BaseFragment implements RetryCallback {

    private static final int PROGRESS = 30;

    SelectCompanyViewModel viewModel;
    FragmentSelectCompanyBinding binding;
    EditText edTxtSearch;

    @Inject
    public CompanyAdapter companyAdapter;
    private boolean isRefreshing = false;

    public static SelectCompanyFragment newInstance() {
        return new SelectCompanyFragment();
    }

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
        return -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectCompanyBinding) getViewDataBinding();
        viewModel.fetchCompanies(this, null);
        companyAdapter.setRetryCallback(this);
        companyAdapter.setListener(listener);
        binding.listCompany.setAdapter(companyAdapter);
        subscribeToLiveData();
        edTxtSearch = binding.includeSearch.edtxtSearch;

        edTxtSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String toSearch = edTxtSearch.getText().toString();
                if (toSearch.equals(""))
                    toSearch = null;
                viewModel.fetchCompanies(SelectCompanyFragment.this,
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
        binding.includeHeader.txtTitle.setText(R.string.select_your_company);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }

    private void subscribeToLiveData() {
        viewModel.companyList.observe(this, new Observer<PagedList<CompanyResponse>>() {
            @Override
            public void onChanged(@Nullable PagedList<CompanyResponse> interestItems) {
                Timber.e("questionList setting list: " + interestItems.size());
                companyAdapter.submitList(interestItems);
                if (companyAdapter.getCurrentList() != null)
                    Timber.e("questionList CURRENT LIST: " + companyAdapter.getCurrentList().size());
            }
        });

        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (companyAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + companyAdapter.getCurrentList().size());

                if (!isRefreshing || networkState.getStatus() == Status.ERROR || networkState.getStatus() == Status.SUCCESS)
                    companyAdapter.setNetworkState(networkState);
            }
        });

        viewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (companyAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + companyAdapter.getCurrentList().size() + " y " + networkState.getStatus().name());
                if (networkState != null) {
                    Timber.e("STATUS: " + networkState.getStatus().name());
                    if (companyAdapter.getCurrentList() != null) {
                        if (networkState.getStatus()
                                == NetworkState.LOADED.getStatus() || networkState.getStatus()
                                == Status.ERROR)
                            isRefreshing = false;
                        Timber.e("STATUS size: " + companyAdapter.getCurrentList().size());

                        boolean d = networkState.getStatus()
                                == NetworkState.LOADING.getStatus();
                        Timber.e("STATUS: " + d);
                    }

                    if (networkState.getStatus() == Status.SUCCESS &&
                            companyAdapter.getCurrentList().size() == 0) {
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

    private CompanyViewHolder.Listener listener = new CompanyViewHolder.Listener() {
        @Override
        public void onCompanyClick(CompanyResponse company) {
            Toast.makeText(getContext(), company.getAttributes().getName(), Toast.LENGTH_SHORT).show();
        }
    };
}
