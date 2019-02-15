package com.divercity.android.features.activity.connectionrequests.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.android.data.entity.group.ConnectionItem;
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class ConnectionRequestsDataSourceFactory extends DataSource.Factory<Long, ConnectionItem> {

    private CompositeDisposable compositeDisposable;
    private FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase;

    private MutableLiveData<ConnectionRequestsDataSource> followersDataSourceMutableLiveData = new MutableLiveData<>();

    public ConnectionRequestsDataSourceFactory(CompositeDisposable compositeDisposable,
                                               FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchConnectionRequestsUseCase = fetchConnectionRequestsUseCase;
    }

    @Override
    public DataSource<Long, ConnectionItem> create() {
        ConnectionRequestsDataSource connectionRequestsDataSource = new ConnectionRequestsDataSource(
                compositeDisposable,
                fetchConnectionRequestsUseCase);
        followersDataSourceMutableLiveData.postValue(connectionRequestsDataSource);
        return connectionRequestsDataSource;
    }

    @NonNull
    public MutableLiveData<ConnectionRequestsDataSource> getGroupsInterestsDataSource() {
        return followersDataSourceMutableLiveData;
    }

}
