package com.divercity.android.features.onboarding.selectmajor.major;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.annotation.Nullable;

import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.major.MajorResponse;
import com.divercity.android.features.onboarding.selectmajor.datasource.MajorDataSource;
import com.divercity.android.features.onboarding.selectmajor.datasource.MajorDataSourceFactory;
import com.divercity.android.features.onboarding.selectmajor.usecase.FetchMajorsUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class MajorPaginatedRepositoryImpl implements PaginatedQueryRepository<MajorResponse> {

    private FetchMajorsUseCase fetchMajorsUseCase;
    private MajorDataSourceFactory majorDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    MajorPaginatedRepositoryImpl(FetchMajorsUseCase fetchMajorsUseCase) {
        this.fetchMajorsUseCase = fetchMajorsUseCase;
    }

    @Override
    public Listing<MajorResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        majorDataSourceFactory = new MajorDataSourceFactory(compositeDisposable, fetchMajorsUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<MajorResponse>> pagedList = new LivePagedListBuilder<>(majorDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(majorDataSourceFactory.getGroupsInterestsDataSource(), MajorDataSource::getNetworkState),
                Transformations.switchMap(majorDataSourceFactory.getGroupsInterestsDataSource(), MajorDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        majorDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        majorDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
