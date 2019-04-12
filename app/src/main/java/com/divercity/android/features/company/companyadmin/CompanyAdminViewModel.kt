package com.divercity.android.features.company.companyadmin

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.features.company.companyadmin.datasource.CompanyAdminRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class CompanyAdminViewModel @Inject
constructor(private val repository : CompanyAdminRepositoryImpl): BaseViewModel(){

    var subscribeToPaginatedLiveData = SingleLiveEvent<CompanyAdminResponse>()
    lateinit var pagedUserList: LiveData<PagedList<CompanyAdminResponse>>
    private lateinit var listingPaginatedJob: Listing<CompanyAdminResponse>
    private lateinit var lastSearch: String

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedJob.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchCompanyAdmins(companyId: String) {
        listingPaginatedJob = repository.fetchData(companyId)
        pagedUserList = listingPaginatedJob.pagedList
    }
}
