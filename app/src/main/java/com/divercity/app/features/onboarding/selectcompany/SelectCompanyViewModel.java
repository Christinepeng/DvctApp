package com.divercity.app.features.onboarding.selectcompany;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.features.onboarding.selectcompany.company.CompanyPaginatedRepositoryImpl;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectCompanyViewModel extends BaseViewModel {

    private LiveData<PagedList<CompanyResponse>> pagedCompanyList;
    private Listing<CompanyResponse> listingPaginatedCompany;
    private CompanyPaginatedRepositoryImpl repository;
    private UserRepository userRepository;

    @Inject
    public SelectCompanyViewModel(CompanyPaginatedRepositoryImpl repository,
                                  UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public LiveData<PagedList<CompanyResponse>> getPagedCompanyList() {
        return pagedCompanyList;
    }
}
