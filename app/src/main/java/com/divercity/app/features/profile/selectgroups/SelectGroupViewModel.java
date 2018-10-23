package com.divercity.app.features.profile.selectgroups;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.profile.selectgroups.group.GroupPaginatedRepository;
import com.divercity.app.features.profile.selectgroups.usecase.JoinGroupUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectGroupViewModel extends BaseViewModel {

    public LiveData<PagedList<GroupResponse>> companyList;
    public MutableLiveData<Resource<GroupResponse>> joinGroupData = new MutableLiveData<>();
    private Listing<GroupResponse> companyListListing;
    private GroupPaginatedRepository repository;
    private UserRepository userRepository;
    private JoinGroupUseCase joinGroupUseCase;

    @Inject
    public SelectGroupViewModel(@NonNull Application application,
                                GroupPaginatedRepository repository,
                                UserRepository userRepository,
                                JoinGroupUseCase joinGroupUseCase) {
        super(application);
        this.repository = repository;
        this.userRepository = userRepository;
        this.joinGroupUseCase = joinGroupUseCase;
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return companyListListing.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return companyListListing.getRefreshState();
    }

    public void fetchCompanies(LifecycleOwner lifecycleOwner,  @Nullable String query){
        if(companyList != null) {
            companyListListing.getNetworkState().removeObservers(lifecycleOwner);
            companyListListing.getRefreshState().removeObservers(lifecycleOwner);
            companyList.removeObservers(lifecycleOwner);
        }
        companyListListing = repository.fetchGroups(query);
        companyList = companyListListing.getPagedList();
    }

    public String getAccountType(){
        return userRepository.getCurrentUserAccountType();
    }

    public void joinGroup(GroupResponse group){
        joinGroupData.postValue(Resource.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<GroupResponse>() {
            @Override
            protected void onFail(String error) {
                joinGroupData.postValue(Resource.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                joinGroupData.postValue(Resource.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(GroupResponse o) {
                joinGroupData.postValue(Resource.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        joinGroupUseCase.execute(callback,JoinGroupUseCase.Params.forJoin(group));
    }

}
