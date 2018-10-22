package com.divercity.app.features.home.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.divercity.app.BR;
import com.divercity.app.R;
import com.divercity.app.core.base.BaseFragment;
import com.divercity.app.core.utils.Preconditions;
import com.divercity.app.data.Status;
import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.databinding.FragmentHomeBinding;
import com.divercity.app.features.home.HomeActivity;
import com.divercity.app.features.home.home.featured.FeaturedAdapter;
import com.divercity.app.features.home.home.feed.adapter.HomeAdapter;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.ui.RetryCallback;
import com.divercity.app.features.onboarding.OnboardingActivity;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;

import timber.log.Timber;

public class HomeFragment extends BaseFragment implements RetryCallback {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private FragmentHomeBinding binding;

    private HomeViewModel viewModel;

    @Inject
    public HomeAdapter feedAdapter;

    @Inject
    UserSharedPreferencesRepository userSharedPreferencesRepository;

    @Inject
    public FeaturedAdapter featuredAdapter;

    private boolean mIsRefreshing = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public ViewModel getViewModel() {
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(HomeViewModel.class);
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentHomeBinding) getViewDataBinding();
        setupToolbar();
        setupEvents();
        initAdapters();
        initSwipeToRefresh();
        subscribeToLiveData();
        viewModel.getFeatured();
    }

    private void setupToolbar() {
        binding.includeToolbar.icon.setVisibility(View.VISIBLE);
        binding.includeToolbar.title.setText(getResources().getString(R.string.divercity));
        ((HomeActivity) getActivity()).setSupportActionBar(binding.includeToolbar.toolbar);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    private void initAdapters() {
        feedAdapter.setRetryCallback(this);
        binding.listMain.setAdapter(feedAdapter);
        binding.listFeatured.setAdapter(featuredAdapter);
    }

    private void subscribeToLiveData() {
        viewModel.questionList.observe(this, new Observer<PagedList<QuestionResponse>>() {
            @Override
            public void onChanged(@Nullable PagedList<QuestionResponse> interestItems) {
                Timber.e( "questionList setting list: " + interestItems.size());
                feedAdapter.submitList(interestItems);
                if(feedAdapter.getCurrentList() != null)
                    Timber.e( "questionList CURRENT LIST: " + feedAdapter.getCurrentList().size());
            }
        });

        viewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if(feedAdapter.getCurrentList() != null)
                    Log.e(TAG, "CURRENT LIST: " + feedAdapter.getCurrentList().size());

                if (!mIsRefreshing || networkState.getStatus() == Status.ERROR || networkState.getStatus() == Status.SUCCESS)
                    feedAdapter.setNetworkState(networkState);

                if (feedAdapter.getItemCount() == 1 && networkState.getStatus() == Status.LOADING) {
                    viewModel.getFeatured();
                }
            }
        });

        viewModel.getRefreshState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if(feedAdapter.getCurrentList() != null)
                    Log.e(TAG, "CURRENT LIST: " + feedAdapter.getCurrentList().size() + " y " + networkState.getStatus().name());
                if (networkState != null) {
                    Log.e(TAG, "STATUS: " + networkState.getStatus().name());
                    if (feedAdapter.getCurrentList() != null) {
                        if (networkState.getStatus()
                                == NetworkState.LOADED.getStatus() || networkState.getStatus()
                                == Status.ERROR)
                            mIsRefreshing = false;
                        Log.e(TAG, "STATUS size: " + feedAdapter.getCurrentList().size());

                        boolean d = networkState.getStatus()
                                == NetworkState.LOADING.getStatus();
                        Log.e(TAG, "STATUS: " + d);

                        binding.swipeListMain.setRefreshing(networkState.getStatus()
                                == NetworkState.LOADING.getStatus());
                    }
                    setInitialLoadingState(networkState);

                    if(networkState.getStatus() == Status.SUCCESS &&
                            feedAdapter.getCurrentList().size() == 0){
                        binding.layNoGroups.setVisibility(View.VISIBLE);
                        binding.fab.setVisibility(View.GONE);
                    } else {
                        binding.layNoGroups.setVisibility(View.GONE);
                        binding.fab.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        viewModel.featuredList.observe(this, resource -> {
            Preconditions.checkNotNull(resource);
            switch (resource.status) {
                case ERROR:
                    break;
                case SUCCESS:
                    featuredAdapter.submitList(resource.data);
                    break;
            }
        });

    }

    @Override
    public void retry() {
        viewModel.retry();
    }

    private void initSwipeToRefresh() {
        binding.swipeListMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                viewModel.getFeatured();
                viewModel.refresh();
            }
        });
        binding.swipeListMain.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark)
                , getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(R.color.colorPrimaryDark));

    }

    private void setInitialLoadingState(NetworkState networkState) {
        if (!mIsRefreshing)
            binding.swipeListMain.setEnabled(networkState.getStatus() == Status.SUCCESS);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fragment_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                userSharedPreferencesRepository.clearUserData();
                Intent intentToLaunch = OnboardingActivity.getCallingIntent(getActivity());
                intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intentToLaunch);
                return true;
            case R.id.action_search:
                showToast();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void setupEvents() {
        binding.includeToolbar.icon.setOnClickListener(view -> showToast());
        binding.fab.setOnClickListener(view -> showToast());
        binding.btnCreateGroup.setOnClickListener(view -> showToast());
        binding.btnExploreGroups.setOnClickListener(view -> showToast());
    }

    private void showToast() {
        Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
    }

}
