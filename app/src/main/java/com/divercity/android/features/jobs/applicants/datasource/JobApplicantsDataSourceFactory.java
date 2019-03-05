package com.divercity.android.features.jobs.applicants.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import com.divercity.android.data.entity.jobapplication.JobApplicationResponse;
import com.divercity.android.features.jobs.applicants.usecase.FetchJobsApplicantsUseCase;

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
