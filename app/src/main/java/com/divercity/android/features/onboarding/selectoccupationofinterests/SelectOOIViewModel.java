package com.divercity.android.features.onboarding.selectoccupationofinterests;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.onboarding.selectoccupationofinterests.datasource.OOIPaginatedRepositoryImpl;
import com.divercity.android.features.onboarding.selectoccupationofinterests.usecase.FollowOOIUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectOOIViewModel extends BaseViewModel {

    private LiveData<PagedList<OOIResponse>> pagedOOIList;
    private Listing<OOIResponse> listingPaginatedOOI;
    private MutableLiveData<Resource<UserResponse>> followOOIResponse = new MutableLiveData<>();
    private OOIPaginatedRepositoryImpl repository;
    private UserRepository userRepository;
    private FollowOOIUseCase followOOIUseCase;
    private SessionRepository sessionRepository;

    @Inject
    public SelectOOIViewModel(OOIPaginatedRepositoryImpl repository,
                              UserRepository userRepository,
                              FollowOOIUseCase followOOIUseCase,
                              SessionRepository sessionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.followOOIUseCase = followOOIUseCase;
        this.sessionRepository = sessionRepository;
        fetchOOI(null, null);
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedOOI.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedOOI.getRefreshState();
    }

    public void fetchOOI(LifecycleOwner lifecycleOwner, @Nullable String query) {
        if (pagedOOIList != null) {
            listingPaginatedOOI.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedOOI.getRefreshState().removeObservers(lifecycleOwner);
            pagedOOIList.removeObservers(lifecycleOwner);
        }
        listingPaginatedOOI = repository.fetchData(query);
        pagedOOIList = listingPaginatedOOI.getPagedList();
    }

    public String getAccountType() {
        return sessionRepository.getAccountType();
    }

    public LiveData<PagedList<OOIResponse>> getPagedOOIList() {
        return pagedOOIList;
    }

    public void updateUserProfile(List<String> ooiIds) {
        followOOIResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<UserResponse>() {
            @Override
            protected void onFail(String error) {
                followOOIResponse.postValue(Resource.Companion.error(error, null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                followOOIResponse.postValue(Resource.Companion.error(error.toString(), null));
            }

            @Override
            protected void onSuccess(UserResponse o) {
                followOOIResponse.postValue(Resource.Companion.success(o));
            }
        };
        followOOIUseCase.execute(callback, FollowOOIUseCase.Params.Companion.forOOI(ooiIds));
    }

    public MutableLiveData<Resource<UserResponse>> getFollowOOIResponse() {
        return followOOIResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        followOOIUseCase.dispose();
    }
}
