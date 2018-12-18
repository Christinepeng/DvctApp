package com.divercity.app.features.industry.selectsingleindustry;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.features.industry.base.industry.IndustryPaginatedRepositoryImpl;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectSingleIndustryViewModel extends BaseViewModel {

    private LiveData<PagedList<IndustryResponse>> pagedIndustryList;
    private Listing<IndustryResponse> listingPaginatedIndustry;
    private IndustryPaginatedRepositoryImpl repository;
    private UserRepository userRepository;

    @Inject
    public SelectSingleIndustryViewModel(IndustryPaginatedRepositoryImpl repository,
                                         UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        fetchIndustries(null, null);
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedIndustry.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedIndustry.getRefreshState();
    }

    public void fetchIndustries(LifecycleOwner lifecycleOwner, @Nullable String query) {
        if (pagedIndustryList != null && lifecycleOwner != null) {
            listingPaginatedIndustry.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedIndustry.getRefreshState().removeObservers(lifecycleOwner);
            pagedIndustryList.removeObservers(lifecycleOwner);
        }
        listingPaginatedIndustry = repository.fetchData(query);
        pagedIndustryList = listingPaginatedIndustry.getPagedList();
    }

    public LiveData<PagedList<IndustryResponse>> getPagedIndustryList() {
        return pagedIndustryList;
    }

    public String getAccountType() {
        return userRepository.getAccountType();
    }
}
