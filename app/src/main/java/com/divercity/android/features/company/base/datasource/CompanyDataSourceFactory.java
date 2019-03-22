package com.divercity.android.features.company.base.datasource;

import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.features.company.base.usecase.FetchCompaniesUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class CompanyDataSourceFactory extends DataSource.Factory<Long, CompanyResponse> {

    private FetchCompaniesUseCase fetchCompaniesUseCase;
    private String query;

    private MutableLiveData<CompanyDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public CompanyDataSourceFactory(FetchCompaniesUseCase fetchCompaniesUseCase,
                                    @Nullable String query) {
        this.fetchCompaniesUseCase = fetchCompaniesUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, CompanyResponse> create() {
        CompanyDataSource companyDataSource = new CompanyDataSource(
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
