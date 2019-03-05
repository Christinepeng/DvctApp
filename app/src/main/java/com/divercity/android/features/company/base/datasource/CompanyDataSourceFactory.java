package com.divercity.android.features.company.base.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.features.company.base.usecase.FetchCompaniesUseCase;
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
