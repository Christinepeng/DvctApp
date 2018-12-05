package com.divercity.app.features.jobs.applicants.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.app.data.entity.jobapplication.JobApplicationResponse;
import com.divercity.app.features.jobs.applicants.usecase.FetchJobsApplicantsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class JobApplicantsDataSourceFactory  extends DataSource.Factory<Long, JobApplicationResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchJobsApplicantsUseCase fetchJobsApplicantsUseCase;

    private MutableLiveData<JobApplicantsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobApplicantsDataSourceFactory(CompositeDisposable compositeDisposable,
                                          FetchJobsApplicantsUseCase fetchJobsApplicantsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsApplicantsUseCase = fetchJobsApplicantsUseCase;
    }

    @Override
    public DataSource<Long, JobApplicationResponse> create() {
        JobApplicantsDataSource jobApplicantsDataSource = new JobApplicantsDataSource(
                compositeDisposable,
                fetchJobsApplicantsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(jobApplicantsDataSource);
        return jobApplicantsDataSource;
    }

    @NonNull
    public MutableLiveData<JobApplicantsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
