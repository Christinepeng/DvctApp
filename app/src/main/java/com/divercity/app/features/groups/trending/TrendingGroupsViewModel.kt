package com.divercity.app.features.groups.trending

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.usecase.JoinGroupUseCase
import com.divercity.app.features.groups.trending.datasource.TrendingGroupsPaginatedRepositoryImpl
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TrendingGroupsViewModel @Inject
constructor(
    private val repository: TrendingGroupsPaginatedRepositoryImpl,
    private val joinGroupUseCase: JoinGroupUseCase
) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    var joinGroupResponse = SingleLiveEvent<Resource<Any>>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
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
        joinGroupResponse.postValue(Resource.loading(null))
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
        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group))
    }
}
