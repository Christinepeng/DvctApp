package com.divercity.android.features.company.mycompanies

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.mycompanies.datasource.MyCompaniesPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class MyCompaniesViewModel @Inject
constructor(
    private val repository: MyCompaniesPaginatedRepositoryImpl
) : BaseViewModel() {

    lateinit var pagedUserList: LiveData<PagedList<CompanyResponse>>
    lateinit var listingPaginatedUser: Listing<CompanyResponse>

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()

    internal var jobId: String? = null
    
    val networkState: LiveData<NetworkState>
        get() = listingPaginatedUser.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedUser.refreshState

    init {
        fetchData(null)
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?) {
        repository.clear()

        listingPaginatedUser = repository.fetchData()
        pagedUserList = listingPaginatedUser.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedUserList.removeObservers(lifecycleOwner)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun retry() {
        repository.retry()
    }

    fun refresh() {
        repository.refresh()
    }
}
