package com.divercity.android.features.activity.connectionrequests.datasource;

import com.divercity.android.data.entity.group.ConnectionItem;
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class ConnectionRequestsDataSourceFactory extends DataSource.Factory<Long, ConnectionItem> {

    private FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase;

    private MutableLiveData<ConnectionRequestsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public ConnectionRequestsDataSourceFactory(FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase) {
        this.fetchConnectionRequestsUseCase = fetchConnectionRequestsUseCase;
    }

    @Override
    public DataSource<Long, ConnectionItem> create() {
        ConnectionRequestsDataSource connectionRequestsDataSource = new ConnectionRequestsDataSource(
                fetchConnectionRequestsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(connectionRequestsDataSource);
        return connectionRequestsDataSource;
    }

    @NonNull
    public MutableLiveData<ConnectionRequestsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }
}
