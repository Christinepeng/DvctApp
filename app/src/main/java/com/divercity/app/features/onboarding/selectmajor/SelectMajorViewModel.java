package com.divercity.app.features.onboarding.selectmajor;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.features.onboarding.selectmajor.major.MajorPaginatedRepositoryImpl;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectMajorViewModel extends BaseViewModel {

    private LiveData<PagedList<MajorResponse>> pagedMajorList;
    private Listing<MajorResponse> listingPaginatedMajor;
    private MajorPaginatedRepositoryImpl repository;
    private UserRepository userRepository;

    @Inject
    public SelectMajorViewModel(MajorPaginatedRepositoryImpl repository,
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
        return listingPaginatedMajor.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedMajor.getRefreshState();
    }

    public void fetchMajors(LifecycleOwner lifecycleOwner, @Nullable String query){
        if(pagedMajorList != null) {
            listingPaginatedMajor.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedMajor.getRefreshState().removeObservers(lifecycleOwner);
            pagedMajorList.removeObservers(lifecycleOwner);
        }
        listingPaginatedMajor = repository.fetchData(query);
        pagedMajorList = listingPaginatedMajor.getPagedList();
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public LiveData<PagedList<MajorResponse>> getPagedMajorList() {
        return pagedMajorList;
    }
}
