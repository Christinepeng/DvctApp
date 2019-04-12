package com.divercity.android.features.chat.recentchats.newrecentchats

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.recentchats.usecase.FetchCurrentChatsUseCase
import com.divercity.android.repository.chat.ChatRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatsViewModel @Inject
constructor(
    private val repository: ChatRepository,
    private val fetchCurrentChatsUseCase: FetchCurrentChatsUseCase
) : BaseViewModel() {

    var showNoRecentMessages = SingleLiveEvent<Boolean>()
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var fetchCurrentChatsResponse = SingleLiveEvent<Resource<Any>>()
    var pagedChatsList: LiveData<PagedList<ExistingUsersChatListItem>>? = null

    private val viewModelJob = Job()

    var pageFetchList = ArrayList<Int>()

    companion object {
        private const val PAGE_SIZE = 10
        private const val THRESHOLD = 0
    }

    init {
        initializePagedList()
    }

    fun initializePagedList() {
        initList()
        fetchRecentChats(0)
    }

    private fun initList() {
//        if (pagedChatsList != null)
//            pagedChatsList?.value?.dataSource?.invalidate()

        val dataSourceFactory = repository.getRecentChats()
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .build()
        pagedChatsList = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
    }

    fun checkIfFetchMoreData(
        visibleItemCount: Int,
        totalItemCount: Int,
        firstVisibleItemPosition: Int
    ) {
        val items = firstVisibleItemPosition + visibleItemCount + THRESHOLD

        if (items % PAGE_SIZE == 0) {
            val page = items / PAGE_SIZE
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchRecentChats(page)
            }
        } else if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
            val page = totalItemCount / PAGE_SIZE + 1
            if (!pageFetchList.contains(page)) {
                pageFetchList.add(page)
                fetchRecentChats(page)
            }
        }
    }

    fun refresh() {
        pageFetchList.clear()
        fetchRecentChats(0)
    }

    fun fetchRecentChats(page: Int) {
        fetchCurrentChatsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<ExistingUsersChatListItem>>() {
            override fun onFail(error: String) {
                pageFetchList.remove(page)
                fetchCurrentChatsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchCurrentChatsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<ExistingUsersChatListItem>) {
                CoroutineScope(Dispatchers.IO + viewModelJob).launch {
                    repository.saveRecentChats(o)
                }

                if (page == 0 && o.isEmpty())
                    showNoRecentMessages.postValue(true)
                else
                    showNoRecentMessages.postValue(false)

                fetchCurrentChatsResponse.postValue(Resource.success(o))
            }
        }
        fetchCurrentChatsUseCase.execute(
            callback, Params(page, PAGE_SIZE, null)
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchCurrentChatsUseCase.dispose()
        viewModelJob.cancel()
    }
}
