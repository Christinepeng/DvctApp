package com.divercity.app.features.groups.onboarding;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.groups.onboarding.group.GroupPaginatedRepositoryImpl;
import com.divercity.app.features.groups.usecase.JoinGroupUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectGroupViewModel extends BaseViewModel {

    private LiveData<PagedList<GroupResponse>> pagedGroupList;
    private Listing<GroupResponse> listingPaginatedSchool;
    private GroupPaginatedRepositoryImpl repository;
    private UserRepository userRepository;

    private JoinGroupUseCase joinGroupUseCase;
    private MutableLiveData<Resource<Boolean>> joinGroupResponse = new MutableLiveData<>();

    @Inject
    public SelectGroupViewModel(GroupPaginatedRepositoryImpl repository,
                                UserRepository userRepository,
                                JoinGroupUseCase joinGroupUseCase) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.joinGroupUseCase = joinGroupUseCase;

        fetchGroups(null, "");
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedSchool.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedSchool.getRefreshState();
    }

    public void fetchGroups(LifecycleOwner lifecycleOwner, @Nullable String query){
        if(pagedGroupList != null) {
            listingPaginatedSchool.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedSchool.getRefreshState().removeObservers(lifecycleOwner);
            pagedGroupList.removeObservers(lifecycleOwner);
        }
        listingPaginatedSchool = repository.fetchData(query);
        pagedGroupList = listingPaginatedSchool.getPagedList();
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public void joinGroup(GroupResponse group){
        joinGroupResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<Boolean>() {
            @Override
            protected void onFail(String error) {
                joinGroupResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                joinGroupResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(Boolean o) {
                joinGroupResponse.postValue(Resource.Companion.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        joinGroupUseCase.execute(callback,JoinGroupUseCase.Params.forJoin(group));
    }

    public MutableLiveData<Resource<Boolean>> getJoinGroupResponse() {
        return joinGroupResponse;
    }

    public LiveData<PagedList<GroupResponse>> getPagedGroupList() {
        return pagedGroupList;
    }
}
