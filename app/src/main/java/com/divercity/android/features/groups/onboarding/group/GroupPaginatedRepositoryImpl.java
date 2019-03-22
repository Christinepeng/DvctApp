package com.divercity.android.features.groups.onboarding.group;

import com.divercity.android.core.base.PaginatedQueryRepository;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.group.group.GroupResponse;
import com.divercity.android.features.groups.onboarding.datasource.GroupDataSource;
import com.divercity.android.features.groups.onboarding.datasource.GroupDataSourceFactory;
import com.divercity.android.features.groups.usecase.FetchGroupsUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class GroupPaginatedRepositoryImpl implements PaginatedQueryRepository<GroupResponse> {

    private FetchGroupsUseCase fetchGroupsUseCase;
    private GroupDataSourceFactory groupDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    GroupPaginatedRepositoryImpl(FetchGroupsUseCase fetchGroupsUseCase) {
        this.fetchGroupsUseCase = fetchGroupsUseCase;
    }

    @Override
    public Listing<GroupResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        groupDataSourceFactory = new GroupDataSourceFactory(compositeDisposable, fetchGroupsUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<GroupResponse>> pagedList = new LivePagedListBuilder<>(groupDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(groupDataSourceFactory.getGroupsInterestsDataSource(), GroupDataSource::getNetworkState),
                Transformations.switchMap(groupDataSourceFactory.getGroupsInterestsDataSource(), GroupDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        groupDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        groupDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
