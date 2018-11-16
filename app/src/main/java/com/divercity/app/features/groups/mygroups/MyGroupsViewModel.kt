package com.divercity.app.features.groups.mygroups

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.mygroups.datasource.MyGroupsPaginatedRepositoryImpl
import com.divercity.app.features.onboarding.selectgroups.usecase.JoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class MyGroupsViewModel @Inject
constructor(private val repository: MyGroupsPaginatedRepositoryImpl,
            private val joinGroupUseCase: JoinGroupUseCase) : BaseViewModel() {

    var joinGroupResponse = SingleLiveEvent<Resource<GroupResponse>>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
    var lastSearch: String? = null

    init {
        fetchGroups("")
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedGroup.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedGroup.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchGroups(searchQuery: String?) {
        searchQuery?.let {
            if (it != lastSearch) {
                lastSearch = it
                repository.compositeDisposable.clear()
                listingPaginatedGroup = repository.fetchData(searchQuery)
                pagedGroupList = listingPaginatedGroup.pagedList
            }
        }
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Resource.loading<GroupResponse>(null))
        val callback = object : DisposableObserverWrapper<GroupResponse>() {
            override fun onFail(error: String) {
                joinGroupResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupResponse) {
                joinGroupResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group))
    }
}
