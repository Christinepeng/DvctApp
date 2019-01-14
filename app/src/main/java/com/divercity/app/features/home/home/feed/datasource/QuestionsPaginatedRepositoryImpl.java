package com.divercity.app.features.home.home.feed.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import com.divercity.app.core.base.PaginatedRepository;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.home.HomeItem;
import com.divercity.app.features.home.home.usecase.FetchFeedRecommendedJobsGroupsUseCase;
import com.divercity.app.features.home.home.usecase.GetQuestionsUseCase;
import io.reactivex.disposables.CompositeDisposable;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lucas on 01/10/2018.
 */

public class QuestionsPaginatedRepositoryImpl implements PaginatedRepository<HomeItem> {

    private GetQuestionsUseCase getQuestionsUseCase;
    private FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase;
    private GroupsInterestsDataSourceFactory groupsInterestsDataSourceFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int pageSize = 20;

    @Inject
    QuestionsPaginatedRepositoryImpl(GetQuestionsUseCase getQuestionsUseCase,
                                     FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase){
        this.getQuestionsUseCase = getQuestionsUseCase;
        this.fetchFeedRecommendedJobsGroupsUseCase = fetchFeedRecommendedJobsGroupsUseCase;
    }

    @Override
    public Listing<HomeItem> fetchData() {

        Executor executor = Executors.newFixedThreadPool(5);

        groupsInterestsDataSourceFactory = new GroupsInterestsDataSourceFactory(
                compositeDisposable,
                getQuestionsUseCase,
                fetchFeedRecommendedJobsGroupsUseCase);

        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setPrefetchDistance(10)
                .setEnablePlaceholders(false)
                .build();

        LiveData<PagedList<HomeItem>> pagedList = new LivePagedListBuilder<>(groupsInterestsDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();

        return new Listing<>(
                pagedList,
                Transformations.switchMap(groupsInterestsDataSourceFactory.getGroupsInterestsDataSource(), GroupsInterestsDataSource::getNetworkState),
                Transformations.switchMap(groupsInterestsDataSourceFactory.getGroupsInterestsDataSource(), GroupsInterestsDataSource::getInitialLoad)
        );
    }

    @Override
    public void retry() {
        groupsInterestsDataSourceFactory.getGroupsInterestsDataSource().getValue().retry();
    }

    @Override
    public void refresh() {
        groupsInterestsDataSourceFactory.getGroupsInterestsDataSource().getValue().invalidate();
    }

    @Override
    public void clear() {
        compositeDisposable.dispose();
    }

}
