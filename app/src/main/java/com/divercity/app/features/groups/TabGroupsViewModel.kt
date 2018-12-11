package com.divercity.app.features.groups

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.Event
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.recommendedgroups.RecommendedGroupsResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.usecase.FetchRecommendedGroupsUseCase
import com.divercity.app.features.groups.usecase.JoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabGroupsViewModel @Inject
constructor(private val fetchRecommendedGroupsUseCase: FetchRecommendedGroupsUseCase,
            private val joinGroupUseCase: JoinGroupUseCase
) : BaseViewModel() {

    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var fetchRecommendedGroupsResponse = MutableLiveData<Resource<List<RecommendedGroupsResponse>>>()

    init {
        fetchRecommendedGroups()
    }

    private fun fetchRecommendedGroups() {
        val callback = object : DisposableObserverWrapper<List<RecommendedGroupsResponse>>() {
            override fun onFail(error: String) {
                fetchRecommendedGroupsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchRecommendedGroupsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<RecommendedGroupsResponse>) {
                fetchRecommendedGroupsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchRecommendedGroupsUseCase.execute(callback, FetchRecommendedGroupsUseCase.Params.forGroups(0,5))
    }

    fun joinGroup(jobId : String?) {
        joinGroupResponse.postValue(Event(Resource.loading(null)))
        val callback = object : DisposableObserverWrapper<Boolean>() {
            override fun onFail(error: String) {
                joinGroupResponse.postValue(Event(Resource.error(error, null)))
            }

            override fun onHttpException(error: JsonElement) {
                joinGroupResponse.postValue(Event(Resource.error(error.toString(), null)))
            }

            override fun onSuccess(o: Boolean) {
                joinGroupResponse.postValue(Event(Resource.success(o)))
            }
        }
        compositeDisposable.add(callback)
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(GroupResponse(jobId)))
    }
}
