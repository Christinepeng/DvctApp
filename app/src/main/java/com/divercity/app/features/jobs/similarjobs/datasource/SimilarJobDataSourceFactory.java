package com.divercity.app.features.jobs.similarjobs.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.app.data.entity.job.response.JobResponse;
import com.divercity.app.features.jobs.similarjobs.usecase.FetchSimilarJobsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class SimilarJobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchSimilarJobsUseCase fetchSimilarJobsUseCase;

    private MutableLiveData<SimilarJobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public SimilarJobDataSourceFactory(CompositeDisposable compositeDisposable,
                                       FetchSimilarJobsUseCase fetchSimilarJobsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchSimilarJobsUseCase = fetchSimilarJobsUseCase;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        SimilarJobDataSource similarJobDataSource = new SimilarJobDataSource(
                compositeDisposable,
                fetchSimilarJobsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(similarJobDataSource);
        return similarJobDataSource;
    }

    @NonNull
    public MutableLiveData<SimilarJobDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
