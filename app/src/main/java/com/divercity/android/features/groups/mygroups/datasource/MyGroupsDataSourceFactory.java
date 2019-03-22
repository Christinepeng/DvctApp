package com.divercity.android.features.groups.mygroups.datasource;

import com.divercity.android.data.entity.group.group.GroupResponse;
import com.divercity.android.features.groups.mygroups.usecase.FetchMyGroupsUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class MyGroupsDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchMyGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<MyGroupsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public MyGroupsDataSourceFactory(CompositeDisposable compositeDisposable,
                                     FetchMyGroupsUseCase fetchGroupsUseCase,
                                     @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        MyGroupsDataSource allGroupsDataSource = new MyGroupsDataSource(
                compositeDisposable,
                fetchGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(allGroupsDataSource);
        return allGroupsDataSource;
    }

    @NonNull
    public MutableLiveData<MyGroupsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
