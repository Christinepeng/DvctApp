package com.divercity.app.features.profile.selectindustry;


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
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.databinding.FragmentSelectCompanyBinding;
import com.divercity.app.features.profile.selectindustry.adapter.IndustryAdapter;
import com.divercity.app.features.profile.selectindustry.adapter.IndustryViewHolder;

import javax.inject.Inject;

import timber.log.Timber;

public class SelectIndustryFragment extends BaseFragment implements RetryCallback {

    private static final int PROGRESS = 30;

    SelectIndustryViewModel viewModel;
    FragmentSelectCompanyBinding binding;
    EditText edTxtSearch;

    @Inject
    public IndustryAdapter industryAdapter;
    private boolean isRefreshing = false;

    public static SelectIndustryFragment newInstance() {
        return new SelectIndustryFragment();
    }

    public SelectIndustryFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_company;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectIndustryViewModel.class);
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
        industryAdapter.setRetryCallback(this);
        industryAdapter.setListener(listener);
        binding.listCompany.setAdapter(industryAdapter);
        subscribeToLiveData();
        edTxtSearch = binding.includeSearch.edtxtSearch;

        edTxtSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String toSearch = edTxtSearch.getText().toString();
                if (toSearch.equals(""))
                    toSearch = null;
                viewModel.fetchCompanies(SelectIndustryFragment.this,
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
        binding.includeHeader.txtTitle.setText(R.string.select_your_indsutry);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }

    private void subscribeToLiveData() {
        viewModel.companyList.observe(this, new Observer<PagedList<IndustryResponse>>() {
            @Override
            public void onChanged(@Nullable PagedList<IndustryResponse> interestItems) {
                Timber.e("questionList setting list: " + interestItems.size());
                industryAdapter.submitList(interestItems);
                if (industryAdapter.getCurrentList() != null)
                    Timber.e("questionList CURRENT LIST: " + industryAdapter.getCurrentList().size());
            }
        });

        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (industryAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + industryAdapter.getCurrentList().size());

                if (!isRefreshing || networkState.getStatus() == Status.ERROR || networkState.getStatus() == Status.SUCCESS)
                    industryAdapter.setNetworkState(networkState);
            }
        });

        viewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (industryAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + industryAdapter.getCurrentList().size() + " y " + networkState.getStatus().name());
                if (networkState != null) {
                    Timber.e("STATUS: " + networkState.getStatus().name());
                    if (industryAdapter.getCurrentList() != null) {
                        if (networkState.getStatus()
                                == NetworkState.LOADED.getStatus() || networkState.getStatus()
                                == Status.ERROR)
                            isRefreshing = false;
                        Timber.e("STATUS size: " + industryAdapter.getCurrentList().size());

                        boolean d = networkState.getStatus()
                                == NetworkState.LOADING.getStatus();
                        Timber.e("STATUS: " + d);
                    }

                    if (networkState.getStatus() == Status.SUCCESS &&
                            industryAdapter.getCurrentList().size() == 0) {
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

    private IndustryViewHolder.Listener listener = new IndustryViewHolder.Listener() {
        @Override
        public void onIndustryClick(IndustryResponse company) {
            Toast.makeText(getContext(), company.getAttributes().getName(), Toast.LENGTH_SHORT).show();
        }
    };
}
