package com.divercity.android.features.singleuseraction

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.singleuseraction.usecase.ShareJobViaMessageUseCase
import com.divercity.android.repository.paginated.UsersByCharacterPaginatedRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class SingleUserActionViewModel @Inject
constructor(
    repository: UsersByCharacterPaginatedRepository,
    private val shareJobViaMessageUseCase: ShareJobViaMessageUseCase
) : BaseViewModelPagination<Any>(repository) {

    var shareJobViaMessageResponse = SingleLiveEvent<Resource<ChatMessageResponse>>()

    init {
        fetchData(null, "")
    }

    fun shareJobViaMessage(otherUserId: String, jobId: String) {
        shareJobViaMessageResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ChatMessageResponse>() {
            override fun onFail(error: String) {
                shareJobViaMessageResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                shareJobViaMessageResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ChatMessageResponse) {
                shareJobViaMessageResponse.postValue(Resource.success(o))
            }
        }
        shareJobViaMessageUseCase.execute(
            callback,
            ShareJobViaMessageUseCase.Params.forJob(otherUserId, jobId)
        )
    }

    override fun onCleared() {
        super.onCleared()
        shareJobViaMessageUseCase.dispose()
    }
}
