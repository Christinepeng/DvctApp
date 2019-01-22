package com.divercity.android.features.company.base;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.features.company.base.company.CompanyPaginatedRepositoryImpl;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectCompanyViewModel extends BaseViewModel {

    private LiveData<PagedList<CompanyResponse>> pagedCompanyList;
    private Listing<CompanyResponse> listingPaginatedCompany;
    private CompanyPaginatedRepositoryImpl repository;

    @Inject
    public SelectCompanyViewModel(CompanyPaginatedRepositoryImpl repository) {
        this.repository = repository;
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedCompany.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedCompany.getRefreshState();
    }

    public void fetchCompanies(LifecycleOwner lifecycleOwner,  @Nullable String query){
        if(pagedCompanyList != null) {
            listingPaginatedCompany.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedCompany.getRefreshState().removeObservers(lifecycleOwner);
            pagedCompanyList.removeObservers(lifecycleOwner);
        }
        listingPaginatedCompany = repository.fetchData(query);
        pagedCompanyList = listingPaginatedCompany.getPagedList();
    }

    public LiveData<PagedList<CompanyResponse>> getPagedCompanyList() {
        return pagedCompanyList;
    }
}
