package com.divercity.android.features.jobs.jobs.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import com.divercity.android.data.entity.job.response.JobResponse;
import com.divercity.android.features.jobs.jobs.usecase.FetchJobsUseCase;

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
