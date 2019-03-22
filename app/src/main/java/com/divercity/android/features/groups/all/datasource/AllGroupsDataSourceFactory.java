package com.divercity.android.features.groups.all.datasource;

import com.divercity.android.data.entity.group.group.GroupResponse;
import com.divercity.android.features.groups.all.usecase.FetchAllGroupsUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class AllGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private FetchAllGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<AllGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public AllGroupsDataSourceFactory(FetchAllGroupsUseCase fetchGroupsUseCase,
                                      @Nullable String query) {
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        AllGroupsDataSource allGroupsDataSource = new AllGroupsDataSource(
                fetchGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(allGroupsDataSource);
        return allGroupsDataSource;
    }

    @NonNull
    public MutableLiveData<AllGroupsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
