package com.divercity.app.features.groups.all

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Event
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.message.MessageResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.all.datasource.AllGroupsPaginatedRepositoryImpl
import com.divercity.app.features.groups.usecase.JoinGroupUseCase
import com.divercity.app.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class AllGroupsViewModel @Inject
constructor(
        private val repository: AllGroupsPaginatedRepositoryImpl,
        private val joinGroupUseCase: JoinGroupUseCase,
        private val requestToJoinUseCase : RequestJoinGroupUseCase
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    private var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    private var lastSearch: String? = null

    val onJoinGroupResponse: LiveData<Event<Resource<Any>>>
        get() = joinGroupResponse

    init {
        fetchGroups(null, "")
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedGroup.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedGroup.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchGroups(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {
                lastSearch = it
                repository.compositeDisposable.clear()
                listingPaginatedGroup = repository.fetchData(it)
                pagedGroupList = listingPaginatedGroup.pagedList

                lifecycleOwner?.let { lifecycleOwner ->
                    removeObservers(lifecycleOwner)
                    subscribeToPaginatedLiveData.call()
                }
            }
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedGroupList.removeObservers(lifecycleOwner)
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Event(Resource.loading(null)))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onSuccess(t: Boolean) {
                joinGroupResponse.postValue(Event(Resource.success(t)))
            }

            override fun onFail(error: String) {
                joinGroupResponse.postValue(Event(Resource.error(error, null)))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Event(Resource.error(error.toString(), null)))
            }
        }

        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group))
    }

    fun requestToJoinGroup(group: GroupResponse) {
        requestToJoinResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: MessageResponse) {
                requestToJoinResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        requestToJoinUseCase.execute(callback,
            RequestJoinGroupUseCase.Params.toJoin(group.id))
    }
}
