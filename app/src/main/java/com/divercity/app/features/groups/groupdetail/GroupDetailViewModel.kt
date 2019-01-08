package com.divercity.app.features.groups.groupdetail

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class GroupDetailViewModel @Inject
constructor(
        private val fetchGroupMembersUseCase: FetchGroupMembersUseCase
) : BaseViewModel() {

    var fetchGroupMembersResponse = SingleLiveEvent<Resource<List<UserResponse>>>()

    fun fetchGroupMembers(group: GroupResponse, page : Int, size: Int, query : String?) {
        fetchGroupMembersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<UserResponse>>() {
            override fun onFail(error: String) {
                fetchGroupMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<UserResponse>) {
                fetchGroupMembersResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchGroupMembersUseCase.execute(callback,
                FetchGroupMembersUseCase.Params.forGroups(group.id, page, size, query))
    }
}
