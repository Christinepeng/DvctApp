package com.divercity.app.features.profile.tabgroups.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class FollowingGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase;
    private String query;

    private MutableLiveData<FollowingGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowingGroupsDataSourceFactory(CompositeDisposable compositeDisposable,
                                            FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase,
                                            @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowedGroupsUseCase = fetchFollowedGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        FollowingGroupsDataSource allGroupsDataSource = new FollowingGroupsDataSource(
                compositeDisposable,
                fetchFollowedGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(allGroupsDataSource);
        return allGroupsDataSource;
    }

    @NonNull
    public MutableLiveData<FollowingGroupsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
