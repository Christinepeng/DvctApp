package com.divercity.android.features.home.home.feed.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.android.data.entity.home.HomeItem;
import com.divercity.android.features.home.home.usecase.FetchFeedRecommendedJobsGroupsUseCase;
import com.divercity.android.features.home.home.usecase.GetQuestionsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class GroupsInterestsDataSourceFactory extends DataSource.Factory<Long, HomeItem> {

    private CompositeDisposable compositeDisposable;
    private GetQuestionsUseCase getQuestionsUseCase;
    private FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase;

    private MutableLiveData<GroupsInterestsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public GroupsInterestsDataSourceFactory(CompositeDisposable compositeDisposable,
                                            GetQuestionsUseCase getQuestionsUseCase,
                                            FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.getQuestionsUseCase = getQuestionsUseCase;
        this.fetchFeedRecommendedJobsGroupsUseCase = fetchFeedRecommendedJobsGroupsUseCase;
    }

    @Override
    public DataSource<Long, HomeItem> create() {
        GroupsInterestsDataSource groupsInterestsDataSource = new GroupsInterestsDataSource(
                getQuestionsUseCase,
                fetchFeedRecommendedJobsGroupsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(groupsInterestsDataSource);
        return groupsInterestsDataSource;
    }

    @NonNull
    public MutableLiveData<GroupsInterestsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}