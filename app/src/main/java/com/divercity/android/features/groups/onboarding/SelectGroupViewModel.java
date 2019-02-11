package com.divercity.android.features.groups.onboarding;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.core.utils.SingleLiveEvent;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.data.entity.message.MessageResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.groups.onboarding.group.GroupPaginatedRepositoryImpl;
import com.divercity.android.features.groups.usecase.JoinGroupUseCase;
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;
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
    private RequestJoinGroupUseCase requestJoinGroupUseCase;
    private SingleLiveEvent<Resource<MessageResponse>> requestToJoinResponse = new SingleLiveEvent<>();

    private JoinGroupUseCase joinGroupUseCase;
    private MutableLiveData<Resource<Boolean>> joinGroupResponse = new MutableLiveData<>();
    private SessionRepository sessionRepository;

    @Inject
    public SelectGroupViewModel(GroupPaginatedRepositoryImpl repository,
                                UserRepository userRepository,
                                JoinGroupUseCase joinGroupUseCase,
                                RequestJoinGroupUseCase requestJoinGroupUseCase,
                                SessionRepository sessionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.joinGroupUseCase = joinGroupUseCase;
        this.requestJoinGroupUseCase = requestJoinGroupUseCase;
        this.sessionRepository = sessionRepository;

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
        return sessionRepository.getAccountType();
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
        joinGroupUseCase.execute(callback,JoinGroupUseCase.Params.forJoin(group));
    }

    public void requestToJoinGroup(GroupResponse group){
        requestToJoinResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<MessageResponse>() {
            @Override
            protected void onFail(String error) {
                requestToJoinResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                requestToJoinResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(MessageResponse o) {
                requestToJoinResponse.postValue(Resource.Companion.success(o));
            }
        };
        requestJoinGroupUseCase.execute(callback,RequestJoinGroupUseCase.Params.Companion.toJoin(group.getId()));
    }

    public MutableLiveData<Resource<Boolean>> getJoinGroupResponse() {
        return joinGroupResponse;
    }

    public LiveData<PagedList<GroupResponse>> getPagedGroupList() {
        return pagedGroupList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        joinGroupUseCase.dispose();
        requestJoinGroupUseCase.dispose();
    }
}
