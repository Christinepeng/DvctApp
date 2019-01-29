package com.divercity.android.features.chat.recentchats.newrecentchats

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.recentchats.usecase.FetchCurrentChatsUseCase
import com.divercity.android.repository.chat.ChatRepositoryImpl
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
constructor(private val repository: ChatRepositoryImpl,
            private val fetchCurrentChatsUseCase: FetchCurrentChatsUseCase
): BaseViewModel(){

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var fetchCurrentChatsResponse = SingleLiveEvent<Resource<Any>>()
    lateinit var pagedChatsList: LiveData<PagedList<ExistingUsersChatListItem>>

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var pageFetchList = ArrayList<Int>()

    companion object {
        private const val PAGE_SIZE = 30
        private const val THRESHOLD = 10
    }

    init {
        initializePagedList()
    }

    fun initializePagedList() {
        val dataSourceFactory = repository.getRecentChats()
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .build()
        pagedChatsList = LivePagedListBuilder(dataSourceFactory, config).build()
        subscribeToPaginatedLiveData.call()
        fetchRecentChats(0)
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

    fun refresh(){
        pageFetchList.clear()
        fetchRecentChats(0)
    }

    fun fetchRecentChats(page : Int) {
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
                uiScope.launch {
                    repository.saveRecentChats(o)
                }
                fetchCurrentChatsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchCurrentChatsUseCase.execute(
            callback, FetchCurrentChatsUseCase.Params.forChat(page, PAGE_SIZE, null)
        )
    }
}
