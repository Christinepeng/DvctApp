package com.divercity.android.features.company.base.company;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.features.company.base.datasource.CompanyDataSource;
import com.divercity.android.features.company.base.datasource.CompanyDataSourceFactory;
import com.divercity.android.features.company.base.usecase.FetchCompaniesUseCase;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lucas on 01/10/2018.
 */

public class CompanyPaginatedRepositoryImpl implements PaginatedQueryRepository<CompanyResponse> {

    private FetchCompaniesUseCase fetchCompaniesUseCase;
    private CompanyDataSourceFactory companyDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    CompanyPaginatedRepositoryImpl(FetchCompaniesUseCase fetchCompaniesUseCase) {
        this.fetchCompaniesUseCase = fetchCompaniesUseCase;
    }

    @Override
    public Listing<CompanyResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        companyDataSourceFactory = new CompanyDataSourceFactory(compositeDisposable, fetchCompaniesUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<CompanyResponse>> pagedList = new LivePagedListBuilder<>(companyDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(companyDataSourceFactory.getGroupsInterestsDataSource(), CompanyDataSource::getNetworkState),
                Transformations.switchMap(companyDataSourceFactory.getGroupsInterestsDataSource(), CompanyDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        companyDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        companyDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
