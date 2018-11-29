package com.divercity.app.features.groups.all.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.groups.onboarding.usecase.FetchGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class AllGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<AllGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public AllGroupsDataSourceFactory(CompositeDisposable compositeDisposable,
                                      FetchGroupsUseCase fetchGroupsUseCase,
                                      @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        AllGroupsDataSource allGroupsDataSource = new AllGroupsDataSource(
                compositeDisposable,
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
