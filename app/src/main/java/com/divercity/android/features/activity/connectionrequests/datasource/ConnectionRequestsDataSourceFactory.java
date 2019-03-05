package com.divercity.android.features.activity.connectionrequests.datasource;

import com.divercity.android.data.entity.group.ConnectionItem;
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
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
