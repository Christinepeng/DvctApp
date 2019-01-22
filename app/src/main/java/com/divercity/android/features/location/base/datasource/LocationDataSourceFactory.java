package com.divercity.android.features.location.base.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.divercity.android.data.entity.location.LocationResponse;
import com.divercity.android.features.location.base.usecase.FetchLocationsUseCase;
import io.reactivex.disposables.CompositeDisposable;

public class LocationDataSourceFactory extends DataSource.Factory<Long, LocationResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchLocationsUseCase fetchLocationsUseCase;
    private String query;

    private MutableLiveData<LocationDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public LocationDataSourceFactory(CompositeDisposable compositeDisposable,
                                     FetchLocationsUseCase fetchLocationsUseCase,
                                     @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchLocationsUseCase = fetchLocationsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, LocationResponse> create() {
        LocationDataSource locationDataSource = new LocationDataSource(
                compositeDisposable,
                fetchLocationsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(locationDataSource);
        return locationDataSource;
    }

    @NonNull
    public MutableLiveData<LocationDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
