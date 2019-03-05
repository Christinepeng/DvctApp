package com.divercity.android.features.location.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.features.location.base.school.LocationPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectLocationViewModel @Inject
constructor(private val repository: LocationPaginatedRepositoryImpl) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedLocationList: LiveData<PagedList<LocationResponse>>

    lateinit var listingPaginatedLocation: Listing<LocationResponse>

    var lastSearch: String? = null

    init {
        fetchLocations(null, "")
    }

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedLocation.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedLocation.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchLocations(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            lastSearch = it
            listingPaginatedLocation = repository.fetchData(it)
            pagedLocationList = listingPaginatedLocation.pagedList

            lifecycleOwner?.let { lifecycleOwner ->
                removeObservers(lifecycleOwner)
                subscribeToPaginatedLiveData.call()
            }
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedLocationList.removeObservers(lifecycleOwner)
    }
}
