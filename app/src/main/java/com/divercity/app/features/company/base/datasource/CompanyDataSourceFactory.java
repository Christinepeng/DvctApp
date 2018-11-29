package com.divercity.app.features.company.base.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.divercity.app.data.entity.company.response.CompanyResponse;
import com.divercity.app.features.company.base.usecase.FetchCompaniesUseCase;
import io.reactivex.disposables.CompositeDisposable;

public class CompanyDataSourceFactory extends DataSource.Factory<Long, CompanyResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchCompaniesUseCase fetchCompaniesUseCase;
    private String query;

    private MutableLiveData<CompanyDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public CompanyDataSourceFactory(CompositeDisposable compositeDisposable,
                                    FetchCompaniesUseCase fetchCompaniesUseCase,
                                    @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchCompaniesUseCase = fetchCompaniesUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, CompanyResponse> create() {
        CompanyDataSource companyDataSource = new CompanyDataSource(
                compositeDisposable,
                fetchCompaniesUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(companyDataSource);
        return companyDataSource;
    }

    @NonNull
    public MutableLiveData<CompanyDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
