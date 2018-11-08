package com.divercity.app.features.jobposting.sharetogroup.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class ShareJobGroupDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase;
    private String query;

    private MutableLiveData<ShareJobGroupDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public ShareJobGroupDataSourceFactory(CompositeDisposable compositeDisposable,
                                          FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase,
                                          @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowedGroupsUseCase = fetchFollowedGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        ShareJobGroupDataSource shareJobGroupDataSource = new ShareJobGroupDataSource(
                compositeDisposable,
                fetchFollowedGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(shareJobGroupDataSource);
        return shareJobGroupDataSource;
    }

    @NonNull
    public MutableLiveData<ShareJobGroupDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
