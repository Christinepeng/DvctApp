package com.divercity.android.features.groups.mygroups

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.mygroups.datasource.MyGroupsPaginatedRepositoryImpl
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class MyGroupsViewModel @Inject
constructor(private val repository: MyGroupsPaginatedRepositoryImpl,
            private val joinGroupUseCase: JoinGroupUseCase,
            private val requestToJoinUseCase : RequestJoinGroupUseCase
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var joinGroupResponse = SingleLiveEvent<Resource<Any>>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
    var requestToJoinResponse = SingleLiveEvent<Resource<MessageResponse>>()
    var lastSearch: String? = null

    init {
        fetchGroups(null,"")
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
                listingPaginatedGroup = repository.fetchData(searchQuery)
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
        joinGroupResponse.postValue(Resource.loading<Boolean>(null))
        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onFail(error: String) {
                joinGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Boolean) {
                joinGroupResponse.postValue(Resource.success(o))
            }
        }
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
        requestToJoinUseCase.execute(callback,
            RequestJoinGroupUseCase.Params.toJoin(group.id))
    }

    override fun onCleared() {
        super.onCleared()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }
}