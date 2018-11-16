package com.divercity.app.features.groups.all

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Event
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.all.datasource.AllGroupsPaginatedRepositoryImpl
import com.divercity.app.features.onboarding.selectgroups.usecase.JoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class AllGroupsViewModel @Inject
constructor(
    private val repository: AllGroupsPaginatedRepositoryImpl,
    private val joinGroupUseCase: JoinGroupUseCase
) : BaseViewModel() {

    private var joinGroupResponse = MutableLiveData<Event<Resource<GroupResponse>>>()
    lateinit var pagedGroupList: LiveData<PagedList<GroupResponse>>
    private lateinit var listingPaginatedGroup: Listing<GroupResponse>
    private var lastSearch: String? = null
    var listSlidingGroups = MutableLiveData<List<GroupResponse>>()

    val onJoinGroupResponse: LiveData<Event<Resource<GroupResponse>>>
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
                lifecycleOwner?.let { lifecycleOwner ->
                    removeObservers(lifecycleOwner)
                }

                lastSearch = it
                repository.compositeDisposable.clear()
                listingPaginatedGroup = repository.fetchData(if (it == "") null else it)
                pagedGroupList = listingPaginatedGroup.pagedList
            }
        }
    }

    fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedGroupList.removeObservers(lifecycleOwner)
    }

    fun joinGroup(group: GroupResponse) {
        joinGroupResponse.postValue(Event(Resource.loading<GroupResponse>(null)))
        val callback = object : DisposableObserverWrapper<GroupResponse>() {
            override fun onFail(error: String) {
                joinGroupResponse.postValue(Event(Resource.error(error, null)))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Event(Resource.error(error.toString(), null)))
            }

            override fun onSuccess(o: GroupResponse) {
                joinGroupResponse.postValue(Event(Resource.success(o)))
            }
        }
        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(group))
    }

    fun selectFiveRandomGroups(groups : PagedList<GroupResponse>){
//        if(listSlidingGroups.value == null || listSlidingGroups.value?.size == 0){
//            var listSelected = ArrayList<GroupResponse>()
//
//            while(listSelected.size != 4){
//                groups.get()
//            }
//        }
    }
}
