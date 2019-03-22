package com.divercity.android.features.groups.followedgroups


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.features.groups.followedgroups.datasource.FollowingGroupsPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class FollowingGroupsViewModel @Inject
constructor(
    private val repository: FollowingGroupsPaginatedRepositoryImpl
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var joinGroupResponse = SingleLiveEvent<Resource<Any>>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
    var lastSearch: String? = null

    fun networkState(): LiveData<NetworkState> = listingPaginatedGroup.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedGroup.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchGroups(lifecycleOwner: LifecycleOwner?, searchQuery: String) {
        if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            repository.compositeDisposable.clear()
            listingPaginatedGroup = repository.fetchData(searchQuery)
            pagedGroupList = listingPaginatedGroup.pagedList

            lifecycleOwner?.let {
                removeObservers(it)
                subscribeToPaginatedLiveData.call()
            }
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedGroupList.removeObservers(lifecycleOwner)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }
}
