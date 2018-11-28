package com.divercity.app.features.jobs.applications.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.jobapplication.JobApplicationResponse;
import com.divercity.app.features.jobs.applications.usecase.FetchJobsApplicationsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobApplicationsDataSourceFactory extends DataSource.Factory<Long, JobApplicationResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsApplicationsUseCase fetchJobsApplicationsUseCase;
    private String query;

    private MutableLiveData<JobApplicationsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobApplicationsDataSourceFactory(CompositeDisposable compositeDisposable,
                                            FetchJobsApplicationsUseCase fetchJobsApplicationsUseCase,
                                            @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsApplicationsUseCase = fetchJobsApplicationsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobApplicationResponse> create() {
        JobApplicationsDataSource jobApplicationsDataSource = new JobApplicationsDataSource(
                compositeDisposable,
                fetchJobsApplicationsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(jobApplicationsDataSource);
        return jobApplicationsDataSource;
    }

    @NonNull
    public MutableLiveData<JobApplicationsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
