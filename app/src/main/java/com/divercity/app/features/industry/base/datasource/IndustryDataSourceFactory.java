package com.divercity.app.features.industry.base.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.features.industry.base.usecase.FetchIndustriesUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class IndustryDataSourceFactory extends DataSource.Factory<Long, IndustryResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchIndustriesUseCase fetchIndustriesUseCase;
    private String query;

    private MutableLiveData<IndustryDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public IndustryDataSourceFactory(CompositeDisposable compositeDisposable,
                                     FetchIndustriesUseCase fetchIndustriesUseCase,
                                     @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchIndustriesUseCase = fetchIndustriesUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, IndustryResponse> create() {
        IndustryDataSource industryDataSource = new IndustryDataSource(
                compositeDisposable,
                fetchIndustriesUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(industryDataSource);
        return industryDataSource;
    }

    @NonNull
    public MutableLiveData<IndustryDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
