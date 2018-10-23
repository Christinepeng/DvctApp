package com.divercity.app.features.profile.selectgroups;


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
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.data.Status;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.databinding.FragmentSelectGroupBinding;
import com.divercity.app.features.profile.selectgroups.adapter.GroupsAdapter;
import com.divercity.app.features.profile.selectgroups.adapter.GroupsViewHolder;

import javax.inject.Inject;

import timber.log.Timber;

public class SelectGroupFragment extends BaseFragment implements RetryCallback {

    private static final int PROGRESS = 95;

    SelectGroupViewModel viewModel;
    FragmentSelectGroupBinding binding;
    EditText edTxtSearch;

    int currentPosition;

    @Inject
    public GroupsAdapter groupsAdapter;
    private boolean isRefreshing = false;

    public static SelectGroupFragment newInstance() {
        return new SelectGroupFragment();
    }

    public SelectGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_group;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectGroupViewModel.class);
        return viewModel;
    }

    @Override
    public int getBindingVariable() {
        return -1;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentSelectGroupBinding) getViewDataBinding();
        viewModel.fetchCompanies(this, null);
        groupsAdapter.setRetryCallback(this);
        groupsAdapter.setListener(listener);
        binding.listGroup.setAdapter(groupsAdapter);
        subscribeToJoinLiveData();
        subscribeToLiveDataPaginated();
        setupHeader();

        binding.btnContinue.setOnClickListener(view1 -> {
            navigator.navigateToNextProfile(getActivity(), viewModel.getAccountType());
        });
    }

    private void setupHeader(){
        edTxtSearch = binding.includeSearch.edtxtSearch;
        edTxtSearch.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String toSearch = edTxtSearch.getText().toString();
                if (toSearch.equals(""))
                    toSearch = null;
                viewModel.fetchCompanies(SelectGroupFragment.this,
                        toSearch);
                subscribeToLiveDataPaginated();
                return true;
            }
            return false;
        });

        binding.includeHeader.progressBar.setMax(100);
        binding.includeHeader.progressBar.setProgress(0);
        binding.includeHeader.progressBar.setProgressWithAnim(PROGRESS);
        binding.includeHeader.txtProgress.setText(PROGRESS + "%");
        binding.includeHeader.txtTitle.setText(R.string.select_group);

        binding.includeHeader.btnClose.setOnClickListener(view1 -> {

        });

        binding.includeHeader.btnSkip.setOnClickListener(view1 -> {

        });
    }

    private void subscribeToLiveDataPaginated() {
        viewModel.companyList.observe(this, new Observer<PagedList<GroupResponse>>() {
            @Override
            public void onChanged(@Nullable PagedList<GroupResponse> interestItems) {
                Timber.e("questionList setting list: " + interestItems.size());
                groupsAdapter.submitList(interestItems);
                if (groupsAdapter.getCurrentList() != null)
                    Timber.e("questionList CURRENT LIST: " + groupsAdapter.getCurrentList().size());
            }
        });

        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (groupsAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + groupsAdapter.getCurrentList().size());

                if (!isRefreshing || networkState.getStatus() == Status.ERROR || networkState.getStatus() == Status.SUCCESS)
                    groupsAdapter.setNetworkState(networkState);
            }
        });

        viewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (groupsAdapter.getCurrentList() != null)
                    Timber.e("CURRENT LIST: " + groupsAdapter.getCurrentList().size() + " y " + networkState.getStatus().name());
                if (networkState != null) {
                    Timber.e("STATUS: " + networkState.getStatus().name());
                    if (groupsAdapter.getCurrentList() != null) {
                        if (networkState.getStatus()
                                == NetworkState.LOADED.getStatus() || networkState.getStatus()
                                == Status.ERROR)
                            isRefreshing = false;
                        Timber.e("STATUS size: " + groupsAdapter.getCurrentList().size());

                        boolean d = networkState.getStatus()
                                == NetworkState.LOADING.getStatus();
                        Timber.e("STATUS: " + d);
                    }

                    if (networkState.getStatus() == Status.SUCCESS &&
                            groupsAdapter.getCurrentList().size() == 0) {
                        binding.txtNoResults.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtNoResults.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void subscribeToJoinLiveData(){
        viewModel.joinGroupData.observe(this, response -> {
            Preconditions.checkNotNull(response);
            binding.includeLoading.setResource(response);
            switch (response.status){
                case ERROR:
                    Toast.makeText(getActivity(),response.message,Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    groupsAdapter.getCurrentList().get(currentPosition).getAttributes().setIsFollowedByCurrent(true);
                    groupsAdapter.notifyItemChanged(currentPosition);
                    break;
            }
        });
    }

    @Override
    public void retry() {
        viewModel.retry();
    }

    private GroupsViewHolder.Listener listener = (position, group) ->{
        currentPosition = position;
        viewModel.joinGroup(group);
    };
}
