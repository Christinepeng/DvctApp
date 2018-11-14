package com.divercity.app.features.groups.trending.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.groups.trending.usecase.FetchTrendingGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class TrendingGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchTrendingGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<TrendingGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public TrendingGroupsDataSourceFactory(CompositeDisposable compositeDisposable,
                                           FetchTrendingGroupsUseCase fetchGroupsUseCase,
                                           @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        TrendingGroupsDataSource allGroupsDataSource = new TrendingGroupsDataSource(
                compositeDisposable,
                fetchGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(allGroupsDataSource);
        return allGroupsDataSource;
    }

    @NonNull
    public MutableLiveData<TrendingGroupsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
