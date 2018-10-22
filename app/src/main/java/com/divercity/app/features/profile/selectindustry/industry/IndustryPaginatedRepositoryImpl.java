package com.divercity.app.features.profile.selectindustry.industry;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.features.profile.selectindustry.datasource.IndustryDataSource;
import com.divercity.app.features.profile.selectindustry.datasource.IndustryDataSourceFactory;
import com.divercity.app.features.profile.selectindustry.usecase.FetchIndustriesUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class IndustryPaginatedRepositoryImpl implements IndustryPaginatedRepository {

    private FetchIndustriesUseCase fetchIndustriesUseCase;
    private IndustryDataSourceFactory industryDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    IndustryPaginatedRepositoryImpl(FetchIndustriesUseCase fetchIndustriesUseCase) {
        this.fetchIndustriesUseCase = fetchIndustriesUseCase;
    }

    @Override
    public Listing<IndustryResponse> fetchCompanies(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        industryDataSourceFactory = new IndustryDataSourceFactory(compositeDisposable, fetchIndustriesUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<IndustryResponse>> pagedList = new LivePagedListBuilder<>(industryDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(industryDataSourceFactory.getGroupsInterestsDataSource(), IndustryDataSource::getNetworkState),
                Transformations.switchMap(industryDataSourceFactory.getGroupsInterestsDataSource(), IndustryDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        industryDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        industryDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
