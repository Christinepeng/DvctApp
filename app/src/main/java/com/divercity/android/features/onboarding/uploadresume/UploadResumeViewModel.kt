package com.divercity.android.features.onboarding.uploadresume

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.base.company.CompanyPaginatedRepositoryImpl
import com.divercity.android.repository.session.SessionRepository

import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class UploadResumeViewModel @Inject
constructor(
    private val repository: CompanyPaginatedRepositoryImpl,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    lateinit var pagedCompanyList: LiveData<PagedList<CompanyResponse>>
    lateinit var listingPaginatedCompany: Listing<CompanyResponse>
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var lastSearch: String? = null

    val accountType: String
        get() = sessionRepository.getAccountType()

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedCompany.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedCompany.refreshState

    fun retry() {
        repository.retry()
    }

    fun refresh() {
        repository.refresh()
    }

    fun fetchCompanies(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, searchQuery)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, query: String?) {
        listingPaginatedCompany = repository.fetchData(query)
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
