package com.divercity.android.features.groups.all.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.features.groups.all.usecase.FetchAllGroupsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class AllGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchAllGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<AllGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public AllGroupsDataSourceFactory(CompositeDisposable compositeDisposable,
                                      FetchAllGroupsUseCase fetchGroupsUseCase,
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
