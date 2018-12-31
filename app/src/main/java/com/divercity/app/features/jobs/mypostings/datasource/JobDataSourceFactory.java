package com.divercity.app.features.jobs.mypostings.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import com.divercity.app.data.entity.job.response.JobResponse;
import com.divercity.app.features.jobs.mypostings.usecase.FetchJobsPostingsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsPostingsUseCase fetchJobsPostingsUseCase;
    private String query;

    private MutableLiveData<JobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobDataSourceFactory(CompositeDisposable compositeDisposable,
                                FetchJobsPostingsUseCase fetchJobsPostingsUseCase,
                                @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsPostingsUseCase = fetchJobsPostingsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        JobDataSource jobDataSource = new JobDataSource(
                compositeDisposable,
                fetchJobsPostingsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(jobDataSource);
        return jobDataSource;
    }

    @NonNull
    public MutableLiveData<JobDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
