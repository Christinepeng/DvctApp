package com.divercity.android.features.industry.onboarding;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.industry.IndustryResponse;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.industry.base.industry.IndustryPaginatedRepositoryImpl;
import com.divercity.android.features.industry.onboarding.usecase.FollowIndustriesUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectIndustryOnboardingViewModel extends BaseViewModel {

    private LiveData<PagedList<IndustryResponse>> pagedIndustryList;
    private Listing<IndustryResponse> listingPaginatedIndustry;
    private IndustryPaginatedRepositoryImpl repository;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private FollowIndustriesUseCase followIndustriesUseCase;
    private MutableLiveData<Resource<UserResponse>> followIndustriesResponse = new MutableLiveData<>();

    @Inject
    public SelectIndustryOnboardingViewModel(IndustryPaginatedRepositoryImpl repository,
                                             FollowIndustriesUseCase followIndustriesUseCase,
                                             UserRepository userRepository,
                                             SessionRepository sessionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.followIndustriesUseCase = followIndustriesUseCase;
        this.sessionRepository = sessionRepository;
        fetchIndustries(null, null);
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedIndustry.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedIndustry.getRefreshState();
    }

    public void fetchIndustries(LifecycleOwner lifecycleOwner, @Nullable String query) {
        if (pagedIndustryList != null && lifecycleOwner != null) {
            listingPaginatedIndustry.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedIndustry.getRefreshState().removeObservers(lifecycleOwner);
            pagedIndustryList.removeObservers(lifecycleOwner);
        }
        listingPaginatedIndustry = repository.fetchData(query);
        pagedIndustryList = listingPaginatedIndustry.getPagedList();
    }

    public LiveData<PagedList<IndustryResponse>> getPagedIndustryList() {
        return pagedIndustryList;
    }

    public String getAccountType() {
        return sessionRepository.getAccountType();
    }

    public void followIndustries(List<String> industriesSelected) {
        followIndustriesResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<UserResponse>() {
            @Override
            protected void onFail(String error) {
                followIndustriesResponse.postValue(Resource.Companion.error(error, null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                followIndustriesResponse.postValue(Resource.Companion.error(error.toString(), null));
            }

            @Override
            protected void onSuccess(UserResponse o) {
                followIndustriesResponse.postValue(Resource.Companion.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        followIndustriesUseCase.execute(callback, FollowIndustriesUseCase.Params.Companion.forIndustry(industriesSelected));
    }

    public MutableLiveData<Resource<UserResponse>> getFollowIndustriesResponse() {
        return followIndustriesResponse;
    }
}
