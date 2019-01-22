package com.divercity.android.features.chat.recentchats.newrecentchats

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.datasource.ChatsPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class ChatsViewModel @Inject
constructor(private val repository: ChatsPaginatedRepositoryImpl): BaseViewModel(){

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedChatsList: LiveData<PagedList<ExistingUsersChatListItem>>
    private lateinit var listingPaginatedChats: Listing<ExistingUsersChatListItem>
    var lastSearch: String? = null

    init {
        fetchChats(null, null)
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedChats.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedChats.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchChats(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, searchQuery)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, searchQuery)
        }
    }

    fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        listingPaginatedChats = repository.fetchData(searchQuery)
        pagedChatsList = listingPaginatedChats.pagedList

        lifecycleOwner?.let { lifecycleOwner ->
            removeObservers(lifecycleOwner)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedChatsList.removeObservers(lifecycleOwner)
    }
}
