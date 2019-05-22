package com.divercity.android.features.groups.groupdetail.about

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.groups.groupdetail.about.usecase.FetchGroupAdminsUseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabAboutGroupDetailViewModel @Inject
constructor(
    private val fetchGroupMembersUseCase: FetchGroupMembersUseCase,
    private val fetchGroupAdminsUseCase: FetchGroupAdminsUseCase
) : BaseViewModel() {

    var fetchGroupMembersResponse = SingleLiveEvent<Resource<List<User>>>()
    var fetchGroupAdminsResponse = SingleLiveEvent<Resource<List<User>>>()

    fun fetchGroupMembers(group: GroupResponse, page: Int, size: Int, query: String?) {
        fetchGroupMembersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<User>>() {
            override fun onFail(error: String) {
                fetchGroupMembersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupMembersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<User>) {
                fetchGroupMembersResponse.postValue(Resource.success(o))
            }
        }
        fetchGroupMembersUseCase.groupId = group.id
        fetchGroupMembersUseCase.execute(
            callback,
            Params(page, size, query)
        )
    }

    fun fetchGroupAdmins(group: GroupResponse, page: Int, size: Int, query: String?) {
        fetchGroupAdminsResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<User>>() {
            override fun onFail(error: String) {
                fetchGroupAdminsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchGroupAdminsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<User>) {
                fetchGroupAdminsResponse.postValue(Resource.success(o))
            }
        }
        fetchGroupAdminsUseCase.groupId = group.id
        fetchGroupAdminsUseCase.execute(
            callback,
            Params(page, size, query)
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchGroupAdminsUseCase.dispose()
        fetchGroupMembersUseCase.dispose()
    }
}
