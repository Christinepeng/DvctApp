package com.divercity.android.features.groups.onboarding.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.features.groups.usecase.FetchGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class GroupDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<GroupDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public GroupDataSourceFactory(CompositeDisposable compositeDisposable,
                                  FetchGroupsUseCase fetchGroupsUseCase,
                                  @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        GroupDataSource groupDataSource = new GroupDataSource(
                compositeDisposable,
                fetchGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(groupDataSource);
        return groupDataSource;
    }

    @NonNull
    public MutableLiveData<GroupDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
