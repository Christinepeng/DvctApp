package com.divercity.app.features.profile.selectgroups.group;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.profile.selectgroups.datasource.GroupDataSource;
import com.divercity.app.features.profile.selectgroups.datasource.GroupDataSourceFactory;
import com.divercity.app.features.profile.selectgroups.usecase.FetchGroupsUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class GroupPaginatedRepositoryImpl implements GroupPaginatedRepository {

    private FetchGroupsUseCase fetchGroupsUseCase;
    private GroupDataSourceFactory groupDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    GroupPaginatedRepositoryImpl(FetchGroupsUseCase fetchGroupsUseCase) {
        this.fetchGroupsUseCase = fetchGroupsUseCase;
    }

    @Override
    public Listing<GroupResponse> fetchGroups(@Nullable String query) {

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
