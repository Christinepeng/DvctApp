package com.divercity.app.features.home.jobs.jobs.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.app.data.entity.job.JobResponse;
import com.divercity.app.features.home.jobs.jobs.usecase.FetchJobsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsUseCase fetchJobsUseCase;

    private MutableLiveData<JobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobDataSourceFactory(CompositeDisposable compositeDisposable,
                                FetchJobsUseCase fetchJobsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsUseCase = fetchJobsUseCase;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        JobDataSource jobDataSource = new JobDataSource(
                compositeDisposable,
                fetchJobsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(jobDataSource);
        return jobDataSource;
    }

    @NonNull
    public MutableLiveData<JobDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
