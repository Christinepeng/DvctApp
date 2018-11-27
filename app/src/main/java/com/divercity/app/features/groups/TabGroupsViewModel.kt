package com.divercity.app.features.groups

import android.arch.lifecycle.MutableLiveData
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.Event
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.group.recommendedgroups.RecommendedGroupsResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.usecase.FetchRecommendedGroupsUseCase
import com.divercity.app.features.onboarding.selectgroups.usecase.JoinGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabGroupsViewModel @Inject
constructor(private val fetchRecommendedGroupsUseCase: FetchRecommendedGroupsUseCase,
            private val joinGroupUseCase: JoinGroupUseCase) : BaseViewModel() {

    var joinGroupResponse = MutableLiveData<Event<Resource<Any>>>()
    var fetchRecommendedGroupsResponse = MutableLiveData<Resource<DataObject<RecommendedGroupsResponse>>>()

    init {
        fetchRecommendedGroups()
    }

    private fun fetchRecommendedGroups() {
        val callback = object : DisposableObserverWrapper<DataObject<RecommendedGroupsResponse>>() {
            override fun onFail(error: String) {
                fetchRecommendedGroupsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchRecommendedGroupsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: DataObject<RecommendedGroupsResponse>) {
                fetchRecommendedGroupsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchRecommendedGroupsUseCase.execute(callback, Any())
    }

    fun joinGroup(jobId : Int) {
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
        joinGroupUseCase.execute(callback, JoinGroupUseCase.Params.forJoin(GroupResponse(jobId.toString())))
    }
}
