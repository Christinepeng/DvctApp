package com.divercity.app.features.profile.selectmajor.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.features.profile.selectmajor.usecase.FetchMajorsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class MajorDataSourceFactory extends DataSource.Factory<Long, MajorResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchMajorsUseCase fetchMajorsUseCase;
    private String query;

    private MutableLiveData<MajorDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public MajorDataSourceFactory(CompositeDisposable compositeDisposable,
                                  FetchMajorsUseCase fetchMajorsUseCase,
                                  @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchMajorsUseCase = fetchMajorsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, MajorResponse> create() {
        MajorDataSource majorDataSource = new MajorDataSource(
                compositeDisposable,
                fetchMajorsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(majorDataSource);
        return majorDataSource;
    }

    @NonNull
    public MutableLiveData<MajorDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
