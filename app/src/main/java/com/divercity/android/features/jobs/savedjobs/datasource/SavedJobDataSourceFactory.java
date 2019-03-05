package com.divercity.android.features.jobs.savedjobs.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import com.divercity.android.data.entity.job.response.JobResponse;
import com.divercity.android.features.jobs.savedjobs.usecase.FetchSavedJobsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class SavedJobDataSourceFactory extends DataSource.Factory<Long, JobResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchSavedJobsUseCase fetchJobsUseCase;
    private String query;

    private MutableLiveData<SavedJobDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public SavedJobDataSourceFactory(CompositeDisposable compositeDisposable,
                                     FetchSavedJobsUseCase fetchJobsUseCase,
                                     @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsUseCase = fetchJobsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, JobResponse> create() {
        SavedJobDataSource savedJobDataSource = new SavedJobDataSource(
                compositeDisposable,
                fetchJobsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(savedJobDataSource);
        return savedJobDataSource;
    }

    @NonNull
    public MutableLiveData<SavedJobDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
