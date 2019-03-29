package com.divercity.android.features.home.people.companies

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.datasource.CompanyPaginatedRepositoryImpl

import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompaniesViewModel @Inject
constructor(private val repository: CompanyPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedCompanyList: LiveData<PagedList<CompanyResponse>>
    lateinit var listingPaginatedCompany: Listing<CompanyResponse>
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    private lateinit var lastSearch: String

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedCompany.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedCompany.refreshState

    init {
        fetchCompanies(null, null)
    }

    fun retry() {
        repository.retry()
    }

    fun refresh() {
        repository.refresh()
    }

    fun fetchCompanies(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, lastSearch)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, lastSearch)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String) {
        repository.clear()

        listingPaginatedCompany = repository.fetchData(searchQuery)
        pagedCompanyList = listingPaginatedCompany.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedCompanyList.removeObservers(lifecycleOwner)
    }
}
