package com.divercity.android.features.onboarding.selectmajor;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.major.MajorResponse;
import com.divercity.android.features.onboarding.selectmajor.major.MajorPaginatedRepositoryImpl;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectMajorViewModel extends BaseViewModel {

    private LiveData<PagedList<MajorResponse>> pagedMajorList;
    private Listing<MajorResponse> listingPaginatedMajor;
    private MajorPaginatedRepositoryImpl repository;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @Inject
    public SelectMajorViewModel(MajorPaginatedRepositoryImpl repository,
                                UserRepository userRepository,
                                SessionRepository sessionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
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
        return sessionRepository.getAccountType();
    }

    public LiveData<PagedList<MajorResponse>> getPagedMajorList() {
        return pagedMajorList;
    }
}
