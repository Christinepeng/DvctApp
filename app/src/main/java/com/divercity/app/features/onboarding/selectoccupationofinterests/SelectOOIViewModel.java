package com.divercity.app.features.onboarding.selectoccupationofinterests;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.data.entity.occupationofinterests.OOIResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.onboarding.selectoccupationofinterests.datasource.OOIPaginatedRepositoryImpl;
import com.divercity.app.features.onboarding.selectoccupationofinterests.usecase.FollowOOIUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import java.util.List;

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

    @Inject
    public SelectOOIViewModel(OOIPaginatedRepositoryImpl repository,
                              UserRepository userRepository,
                              FollowOOIUseCase followOOIUseCase) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.followOOIUseCase = followOOIUseCase;
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
        return userRepository.getAccountType();
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
        getCompositeDisposable().add(callback);
        followOOIUseCase.execute(callback, FollowOOIUseCase.Params.Companion.forOOI(ooiIds));
    }

    public MutableLiveData<Resource<UserResponse>> getFollowOOIResponse() {
        return followOOIResponse;
    }
}
