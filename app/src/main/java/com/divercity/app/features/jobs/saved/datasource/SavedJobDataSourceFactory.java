package com.divercity.app.features.jobs.saved.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;
import com.divercity.app.data.entity.job.response.JobResponse;
import com.divercity.app.features.jobs.saved.usecase.FetchSavedJobsUseCase;

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
