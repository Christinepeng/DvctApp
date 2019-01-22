package com.divercity.android.features.onboarding.selectschool.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.features.onboarding.selectschool.usecase.FetchSchoolUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class SchoolDataSourceFactory extends DataSource.Factory<Long, SchoolResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchSchoolUseCase fetchSchoolUseCase;
    private String query;

    private MutableLiveData<SchoolDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public SchoolDataSourceFactory(CompositeDisposable compositeDisposable,
                                   FetchSchoolUseCase fetchSchoolUseCase,
                                   @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchSchoolUseCase = fetchSchoolUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, SchoolResponse> create() {
        SchoolDataSource schoolDataSource = new SchoolDataSource(
                compositeDisposable,
                fetchSchoolUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(schoolDataSource);
        return schoolDataSource;
    }

    @NonNull
    public MutableLiveData<SchoolDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
