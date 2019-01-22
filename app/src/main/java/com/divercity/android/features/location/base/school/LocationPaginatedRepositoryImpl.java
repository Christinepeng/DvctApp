package com.divercity.android.features.location.base.school;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.location.LocationResponse;
import com.divercity.android.features.location.base.datasource.LocationDataSource;
import com.divercity.android.features.location.base.datasource.LocationDataSourceFactory;
import com.divercity.android.features.location.base.usecase.FetchLocationsUseCase;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
