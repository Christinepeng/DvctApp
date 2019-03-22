package com.divercity.android.features.groups.onboarding;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.group.group.GroupResponse;
import com.divercity.android.features.groups.onboarding.group.GroupPaginatedRepositoryImpl;
import com.divercity.android.features.groups.usecase.JoinGroupUseCase;
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectGroupViewModel extends BaseViewModel {

    private LiveData<PagedList<GroupResponse>> pagedGroupList;
    private Listing<GroupResponse> listingPaginatedSchool;
    private GroupPaginatedRepositoryImpl repository;
    private UserRepository userRepository;
    private RequestJoinGroupUseCase requestJoinGroupUseCase;

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
