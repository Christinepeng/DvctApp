package com.divercity.android.features.jobs.mypostings.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import com.divercity.android.data.entity.job.response.JobResponse;
import com.divercity.android.features.jobs.mypostings.usecase.FetchJobsPostingsUseCase;

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
