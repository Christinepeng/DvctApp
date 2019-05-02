package com.divercity.android.features.industry.onboarding;

import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.industry.IndustryResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.industry.base.industry.IndustryPaginatedRepositoryImpl;
import com.divercity.android.features.industry.onboarding.usecase.FollowIndustriesUseCase;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.session.SessionRepository;
import com.google.gson.JsonElement;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectIndustryOnboardingViewModel extends BaseViewModel {

    private LiveData<PagedList<IndustryResponse>> pagedIndustryList;
    private Listing<IndustryResponse> listingPaginatedIndustry;
    private IndustryPaginatedRepositoryImpl repository;
    private SessionRepository sessionRepository;
    private FollowIndustriesUseCase followIndustriesUseCase;
    private MutableLiveData<Resource<User>> followIndustriesResponse = new MutableLiveData<>();

    @Inject
    public SelectIndustryOnboardingViewModel(IndustryPaginatedRepositoryImpl repository,
                                             FollowIndustriesUseCase followIndustriesUseCase,
                                             SessionRepository sessionRepository) {
        this.repository = repository;
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
        DisposableObserverWrapper callback = new DisposableObserverWrapper<User>() {
            @Override
            protected void onFail(String error) {
                followIndustriesResponse.postValue(Resource.Companion.error(error, null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                followIndustriesResponse.postValue(Resource.Companion.error(error.toString(), null));
            }

            @Override
            protected void onSuccess(User o) {
                followIndustriesResponse.postValue(Resource.Companion.success(o));
            }
        };
        followIndustriesUseCase.execute(callback, FollowIndustriesUseCase.Params.Companion.forIndustry(industriesSelected));
    }

    public MutableLiveData<Resource<User>> getFollowIndustriesResponse() {
        return followIndustriesResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        followIndustriesUseCase.dispose();
    }
}
