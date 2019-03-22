package com.divercity.android.features.groups.all

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
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.all.datasource.AllGroupsPaginatedRepositoryImpl
import com.divercity.android.features.groups.all.model.GroupPositionModel
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import com.divercity.android.features.groups.usecase.RequestJoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class AllGroupsViewModel @Inject
constructor(
    private val repository: AllGroupsPaginatedRepositoryImpl,
    private val joinGroupUseCase: JoinGroupUseCase,
    private val requestToJoinUseCase: RequestJoinGroupUseCase
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>

    var requestToJoinPrivateGroupResponse = SingleLiveEvent<Resource<GroupPositionModel>>()
    var joinPublicGroupResponse = SingleLiveEvent<Resource<GroupPositionModel>>()

    private var lastSearch: String? = null

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
                repository.clear()
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

    fun joinGroup(group: GroupPositionModel) {
        joinPublicGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onSuccess(t: Boolean) {
                joinPublicGroupResponse.postValue(Resource.success(group))
            }

            override fun onFail(error: String) {
                joinPublicGroupResponse.postValue(Resource.error(error, group))
            }

            override fun onHttpException(error: JsonElement) {
                joinPublicGroupResponse.postValue(Resource.error(error.toString(), group))
            }
        }
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group.group.id))
    }

    fun requestToJoinGroup(group: GroupPositionModel) {
        requestToJoinPrivateGroupResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<MessageResponse>() {
            override fun onFail(error: String) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error, group))
            }

            override fun onHttpException(error: JsonElement) {
                requestToJoinPrivateGroupResponse.postValue(Resource.error(error.toString(), group))
            }

            override fun onSuccess(o: MessageResponse) {
                requestToJoinPrivateGroupResponse.postValue(Resource.success(group))
            }
        }
        requestToJoinUseCase.execute(
            callback,
            RequestJoinGroupUseCase.Params.toJoin(group.group.id)
        )
    }

    override fun onCleared() {
        super.onCleared()
        requestToJoinUseCase.dispose()
        joinGroupUseCase.dispose()
    }
}
