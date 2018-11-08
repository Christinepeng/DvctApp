package com.divercity.app.features.jobposting.sharetogroup.group;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.PaginatedQueryRepository;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.features.jobposting.sharetogroup.datasource.ShareJobGroupDataSource;
import com.divercity.app.features.jobposting.sharetogroup.datasource.ShareJobGroupDataSourceFactory;
import com.divercity.app.features.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 01/10/2018.
 */

public class ShareJobGroupPaginatedRepositoryImpl implements PaginatedQueryRepository<GroupResponse> {

    private FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase;
    private ShareJobGroupDataSourceFactory shareJobGroupDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    ShareJobGroupPaginatedRepositoryImpl(FetchFollowedGroupsUseCase fetchFollowedGroupsUseCase) {
        this.fetchFollowedGroupsUseCase = fetchFollowedGroupsUseCase;
    }

    @Override
    public Listing<GroupResponse> fetchData(@Nullable String query) {

        Executor executor = Executors.newFixedThreadPool(5);

        shareJobGroupDataSourceFactory = new ShareJobGroupDataSourceFactory(compositeDisposable, fetchFollowedGroupsUseCase, query);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<GroupResponse>> pagedList = new LivePagedListBuilder<>(shareJobGroupDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(shareJobGroupDataSourceFactory.getGroupsInterestsDataSource(), ShareJobGroupDataSource::getNetworkState),
                Transformations.switchMap(shareJobGroupDataSourceFactory.getGroupsInterestsDataSource(), ShareJobGroupDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        shareJobGroupDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        shareJobGroupDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
