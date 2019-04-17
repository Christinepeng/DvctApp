package com.divercity.android.features.multipleuseraction

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.multipleuseraction.usecase.AddGroupAdminUseCase
import com.divercity.android.repository.paginated.UsersByCharacterPaginatedRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class MultipleUserActionViewModel @Inject
constructor(
    repository: UsersByCharacterPaginatedRepository,
    private val addGroupAdminUseCase: AddGroupAdminUseCase
) : BaseViewModelPagination<Any>(repository) {

    var addGroupAdminResponse = SingleLiveEvent<Resource<String>>()

    init {
        fetchData(null, "")
    }

    fun addGroupAdmin(groupId: String, userIds: List<String>) {
        addGroupAdminResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<String>() {
            override fun onFail(error: String) {
                addGroupAdminResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addGroupAdminResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: String) {
                addGroupAdminResponse.postValue(Resource.success(o))
            }
        }
        addGroupAdminUseCase.execute(
            callback,
            AddGroupAdminUseCase.Params.forAdmin(groupId, userIds)
        )
    }
}
