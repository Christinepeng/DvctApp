package com.divercity.app.features.home.jobs.jobs.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import com.divercity.app.data.entity.job.JobResponse;
import com.divercity.app.features.home.jobs.jobs.usecase.FetchJobsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsUseCase fetchJobsUseCase;
    private String query;

    private MutableLiveData<JobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobDataSourceFactory(CompositeDisposable compositeDisposable,
                                FetchJobsUseCase fetchJobsUseCase,
                                @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsUseCase = fetchJobsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        JobDataSource jobDataSource = new JobDataSource(
                compositeDisposable,
                fetchJobsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(jobDataSource);
        return jobDataSource;
    }

    @NonNull
    public MutableLiveData<JobDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
