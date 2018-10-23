package com.divercity.app.features.profile.selectmajor;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.features.profile.selectmajor.major.MajorPaginatedRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectMajorViewModel extends BaseViewModel {

    public LiveData<PagedList<MajorResponse>> companyList;
    private Listing<MajorResponse> companyListListing;
    MajorPaginatedRepository repository;

    @Inject
    public SelectMajorViewModel(@NonNull Application application,
                                MajorPaginatedRepository repository) {
        super(application);
        this.repository = repository;
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return companyListListing.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return companyListListing.getRefreshState();
    }

    public void fetchMajors(LifecycleOwner lifecycleOwner, @Nullable String query){
        if(companyList != null) {
            companyListListing.getNetworkState().removeObservers(lifecycleOwner);
            companyListListing.getRefreshState().removeObservers(lifecycleOwner);
            companyList.removeObservers(lifecycleOwner);
        }
        companyListListing = repository.fetchMajors(query);
        companyList = companyListListing.getPagedList();
    }
}
