package com.divercity.android.features.industry.selectsingleindustry;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.annotation.Nullable;

import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.entity.industry.IndustryResponse;
import com.divercity.android.features.industry.base.industry.IndustryPaginatedRepositoryImpl;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectSingleIndustryViewModel extends BaseViewModel {

    private LiveData<PagedList<IndustryResponse>> pagedIndustryList;
    private Listing<IndustryResponse> listingPaginatedIndustry;
    private IndustryPaginatedRepositoryImpl repository;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;

    @Inject
    public SelectSingleIndustryViewModel(IndustryPaginatedRepositoryImpl repository,
                                         UserRepository userRepository,
                                         SessionRepository sessionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
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
        return sessionRepository.getAccountType();
    }
}
