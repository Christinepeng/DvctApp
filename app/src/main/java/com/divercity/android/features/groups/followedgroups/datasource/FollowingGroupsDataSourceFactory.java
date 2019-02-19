package com.divercity.android.features.groups.followedgroups.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.features.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase;

public class FollowingGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase;
    private String query;

    private MutableLiveData<FollowingGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowingGroupsDataSourceFactory(FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase,
                                            @Nullable String query) {
        this.fetchFollowedGroupsUseCase = fetchFollowedGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        FollowingGroupsDataSource allGroupsDataSource = new FollowingGroupsDataSource(
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
