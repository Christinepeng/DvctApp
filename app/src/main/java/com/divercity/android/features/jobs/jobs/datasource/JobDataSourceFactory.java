package com.divercity.android.features.jobs.jobs.datasource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import com.divercity.android.data.entity.job.response.JobResponse;
import com.divercity.android.features.jobs.jobs.usecase.FetchJobsUseCase;

public class JobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private FetchJobsUseCase fetchJobsUseCase;
    private String query;

    private MutableLiveData<JobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public JobDataSourceFactory(FetchJobsUseCase fetchJobsUseCase,
                                @Nullable String query) {
        this.fetchJobsUseCase = fetchJobsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        JobDataSource jobDataSource = new JobDataSource(
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
