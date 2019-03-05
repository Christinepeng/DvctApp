package com.divercity.android.features.onboarding.selectschool.school;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.annotation.Nullable;

import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.features.onboarding.selectschool.datasource.SchoolDataSource;
import com.divercity.android.features.onboarding.selectschool.datasource.SchoolDataSourceFactory;
import com.divercity.android.features.onboarding.selectschool.usecase.FetchSchoolUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class SchoolPaginatedRepositoryImpl implements PaginatedQueryRepository<SchoolResponse> {

    private FetchSchoolUseCase fetchSchoolUseCase;
    private SchoolDataSourceFactory schoolDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    SchoolPaginatedRepositoryImpl(FetchSchoolUseCase fetchSchoolUseCase) {
        this.fetchSchoolUseCase = fetchSchoolUseCase;
    }

    @Override
    public Listing<SchoolResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        schoolDataSourceFactory = new SchoolDataSourceFactory(compositeDisposable, fetchSchoolUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<SchoolResponse>> pagedList = new LivePagedListBuilder<>(schoolDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(schoolDataSourceFactory.getGroupsInterestsDataSource(), SchoolDataSource::getNetworkState),
                Transformations.switchMap(schoolDataSourceFactory.getGroupsInterestsDataSource(), SchoolDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        schoolDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        schoolDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
