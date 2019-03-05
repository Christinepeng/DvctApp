package com.divercity.android.features.jobs.jobposting.sharetogroup.datasource;

import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class FollowedGroupsSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase;
    private String query;

    private MutableLiveData<FollowedGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowedGroupsSourceFactory(FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase,
                                       @Nullable String query) {
        this.fetchFollowedGroupsUseCase = fetchFollowedGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        FollowedGroupsDataSource followedGroupsDataSource = new FollowedGroupsDataSource(
                fetchFollowedGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(followedGroupsDataSource);
        return followedGroupsDataSource;
    }

    @NonNull
    public MutableLiveData<FollowedGroupsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
