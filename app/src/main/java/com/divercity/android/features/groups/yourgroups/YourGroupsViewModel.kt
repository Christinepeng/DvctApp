package com.divercity.android.features.groups.yourgroups

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase
import com.divercity.android.model.Question
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class YourGroupsViewModel @Inject
constructor(
    repository: PopularGroupQuestionsPaginatedRepository,
    private val fetchFollowedGroupsUseCase: FetchFollowedGroupsUseCase
) : BaseViewModelPagination<Question>(repository) {

    val fetchMyGroupsResponse = MutableLiveData<Resource<List<GroupResponse>>>()

    val showFollowedGroupSection = MutableLiveData<Boolean>()

    init {
        showFollowedGroupSection.value = false
        fetchData()
        fetchMyGroups()
    }

    fun fetchMyGroups() {
        fetchMyGroupsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<GroupResponse>>() {
            override fun onFail(error: String) {
                fetchMyGroupsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchMyGroupsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<GroupResponse>) {
                fetchMyGroupsResponse.postValue(Resource.success(o))
            }
        }
        fetchFollowedGroupsUseCase.execute(callback, Params(0, 3, null))
    }
}
