package com.divercity.android.features.jobs.jobposting.sharetogroup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.data.entity.job.response.JobResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.jobs.jobposting.sharetogroup.group.ShareJobGroupPaginatedRepositoryImpl;
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.ShareJobGroupsUseCase;
import com.google.gson.JsonElement;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class ShareJobGroupViewModel extends BaseViewModel {

    private LiveData<PagedList<GroupResponse>> pagedGroupList;
    private Listing<GroupResponse> listingPaginatedGroups;
    private ShareJobGroupPaginatedRepositoryImpl repository;

    private MutableLiveData<Resource<JobResponse>> shareJobGroupsResponse = new MutableLiveData<>();

    private ShareJobGroupsUseCase shareJobGroupsUseCase;
    String jobId;

    @Inject
    public ShareJobGroupViewModel(ShareJobGroupPaginatedRepositoryImpl repository,
                                  ShareJobGroupsUseCase shareJobGroupsUseCase) {
        this.repository = repository;
        this.shareJobGroupsUseCase = shareJobGroupsUseCase;
        fetchFollowedGroups(null);
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedGroups.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedGroups.getRefreshState();
    }

    public void fetchFollowedGroups(@Nullable String query){
        listingPaginatedGroups = repository.fetchData(query);
        pagedGroupList = listingPaginatedGroups.getPagedList();
    }

    public void shareJobs(List<String> groupsIds){
        shareJobGroupsResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<JobResponse>() {
            @Override
            protected void onFail(String error) {
                shareJobGroupsResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                shareJobGroupsResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(JobResponse o) {
                shareJobGroupsResponse.postValue(Resource.Companion.success(o));
            }
        };
        shareJobGroupsUseCase.execute(callback,ShareJobGroupsUseCase.Params.Companion.forShare(jobId,groupsIds));
    }

    public MutableLiveData<Resource<JobResponse>> getShareJobGroupsResponse() {
        return shareJobGroupsResponse;
    }

    public LiveData<PagedList<GroupResponse>> getPagedGroupList() {
        return pagedGroupList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        shareJobGroupsUseCase.dispose();
    }
}
