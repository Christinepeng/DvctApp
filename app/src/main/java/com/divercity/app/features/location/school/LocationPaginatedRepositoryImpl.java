package com.divercity.app.features.location.school;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.PaginatedQueryRepository;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.features.location.datasource.LocationDataSource;
import com.divercity.app.features.location.datasource.LocationDataSourceFactory;
import com.divercity.app.features.location.usecase.FetchLocationsUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class LocationPaginatedRepositoryImpl implements PaginatedQueryRepository<LocationResponse> {

    private FetchLocationsUseCase fetchLocationsUseCase;
    private LocationDataSourceFactory locationDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    LocationPaginatedRepositoryImpl(FetchLocationsUseCase fetchLocationsUseCase) {
        this.fetchLocationsUseCase = fetchLocationsUseCase;
    }

    @Override
    public Listing<LocationResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        locationDataSourceFactory = new LocationDataSourceFactory(compositeDisposable, fetchLocationsUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<LocationResponse>> pagedList = new LivePagedListBuilder<>(locationDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(locationDataSourceFactory.getGroupsInterestsDataSource(), LocationDataSource::getNetworkState),
                Transformations.switchMap(locationDataSourceFactory.getGroupsInterestsDataSource(), LocationDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        locationDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        locationDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
