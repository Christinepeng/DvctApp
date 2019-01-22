package com.divercity.android.features.onboarding.selectoccupationofinterests.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.features.onboarding.selectoccupationofinterests.usecase.FetchOOIUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class OOIPaginatedRepositoryImpl implements PaginatedQueryRepository<OOIResponse> {

    private FetchOOIUseCase fetchOOIUseCase;
    private OOIDataSourceFactory OOIDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    OOIPaginatedRepositoryImpl(FetchOOIUseCase fetchOOIUseCase) {
        this.fetchOOIUseCase = fetchOOIUseCase;
    }

    @Override
    public Listing<OOIResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        OOIDataSourceFactory = new OOIDataSourceFactory(compositeDisposable, fetchOOIUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<OOIResponse>> pagedList = new LivePagedListBuilder<>(OOIDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(OOIDataSourceFactory.getGroupsInterestsDataSource(), OOIDataSource::getNetworkState),
                Transformations.switchMap(OOIDataSourceFactory.getGroupsInterestsDataSource(), OOIDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        OOIDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        OOIDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
