package com.divercity.app.features.location

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.location.school.LocationPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectLocationViewModel @Inject
constructor(private val repository: LocationPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedLocationList: LiveData<PagedList<LocationResponse>>

    lateinit var listingPaginatedLocation: Listing<LocationResponse>

    init {
        fetchLocations(null)
    }

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedLocation.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedLocation.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchLocations(query: String?) {
        listingPaginatedLocation = repository.fetchData(query)
        pagedLocationList = listingPaginatedLocation.pagedList
    }
}
