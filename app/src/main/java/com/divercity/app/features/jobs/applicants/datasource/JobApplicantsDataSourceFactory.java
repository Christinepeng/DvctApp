package com.divercity.app.features.jobs.applicants.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import com.divercity.app.data.entity.job.response.JobResponse;
import com.divercity.app.features.jobs.applications.datasource.JobApplicationsDataSource;
import com.divercity.app.features.jobs.applications.usecase.FetchJobsApplicationsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobApplicantsDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsApplicationsUseCase fetchJobsApplicationsUseCase;
    private String query;

    private MutableLiveData<com.divercity.app.features.jobs.applications.datasource.JobApplicationsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobApplicantsDataSourceFactory(CompositeDisposable compositeDisposable,
                                          FetchJobsApplicationsUseCase fetchJobsApplicationsUseCase,
                                          @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsApplicationsUseCase = fetchJobsApplicationsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        com.divercity.app.features.jobs.applications.datasource.JobApplicationsDataSource jobApplicationsDataSource = new com.divercity.app.features.jobs.applications.datasource.JobApplicationsDataSource(
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
