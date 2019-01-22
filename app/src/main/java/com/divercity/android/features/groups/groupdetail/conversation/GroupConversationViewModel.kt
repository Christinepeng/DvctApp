package com.divercity.android.features.groups.groupdetail.conversation

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.features.groups.groupdetail.conversation.datasource.GroupConversationPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class GroupConversationViewModel @Inject
constructor(
        private val repository: GroupConversationPaginatedRepositoryImpl) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedConversationList: LiveData<PagedList<QuestionResponse>>
    private lateinit var listingPaginatedConversation: Listing<QuestionResponse>
    private var lastSearch: String? = null

    fun networkState(): LiveData<NetworkState> = listingPaginatedConversation.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedConversation.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchConversations(lifecycleOwner: LifecycleOwner?, groupId : String, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner,groupId, searchQuery)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner,groupId, searchQuery)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, groupId : String, searchQuery: String?) {
        listingPaginatedConversation = repository.fetchData(groupId, searchQuery)
        pagedConversationList = listingPaginatedConversation.pagedList

        lifecycleOwner?.let { lifecycleOwner ->
            removeObservers(lifecycleOwner)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedConversationList.removeObservers(lifecycleOwner)
    }
}
