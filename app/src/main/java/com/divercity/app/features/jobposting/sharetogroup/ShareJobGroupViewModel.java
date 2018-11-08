package com.divercity.app.features.jobposting.sharetogroup;

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
import com.divercity.app.features.jobposting.sharetogroup.group.ShareJobGroupPaginatedRepositoryImpl;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class ShareJobGroupViewModel extends BaseViewModel {

    private LiveData<PagedList<GroupResponse>> pagedGroupList;
    private Listing<GroupResponse> listingPaginatedSchool;
    private ShareJobGroupPaginatedRepositoryImpl repository;
    private UserRepository userRepository;

//    private JoinGroupUseCase joinGroupUseCase;
    private MutableLiveData<Resource<GroupResponse>> joinGroupResponse = new MutableLiveData<>();

    @Inject
    public ShareJobGroupViewModel(ShareJobGroupPaginatedRepositoryImpl repository,
                                  UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    public void fetchCompanies(LifecycleOwner lifecycleOwner,  @Nullable String query){
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
//        joinGroupResponse.postValue(Resource.Companion.loading(null));
//        DisposableObserverWrapper callback = new DisposableObserverWrapper<GroupResponse>() {
//            @Override
//            protected void onFail(String error) {
//                joinGroupResponse.postValue(Resource.Companion.error(error,null));
//            }
//
//            @Override
//            protected void onHttpException(JsonElement error) {
//                joinGroupResponse.postValue(Resource.Companion.error(error.toString(),null));
//            }
//
//            @Override
//            protected void onSuccess(GroupResponse o) {
//                joinGroupResponse.postValue(Resource.Companion.success(o));
//            }
//        };
//        getCompositeDisposable().add(callback);
//        joinGroupUseCase.execute(callback,JoinGroupUseCase.Params.forJoin(group));
    }

    public MutableLiveData<Resource<GroupResponse>> getJoinGroupResponse() {
        return joinGroupResponse;
    }

    public LiveData<PagedList<GroupResponse>> getPagedGroupList() {
        return pagedGroupList;
    }
}
