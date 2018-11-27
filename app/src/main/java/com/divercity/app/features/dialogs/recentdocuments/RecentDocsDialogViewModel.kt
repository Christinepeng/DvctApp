package com.divercity.app.features.dialogs.recentdocuments

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.dialogs.recentdocuments.usecase.FetchRecentDocumentsUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 23/11/2018.
 */

class RecentDocsDialogViewModel @Inject
constructor(
        private val fetchRecentDocumentsUseCase: FetchRecentDocumentsUseCase) : BaseViewModel() {

    var fetchRecentDocumentsResponse = SingleLiveEvent<Resource<List<DocumentResponse>>>()

    fun fetchRecentDocs() {
        fetchRecentDocumentsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<DocumentResponse>>() {
            override fun onFail(error: String) {
                fetchRecentDocumentsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchRecentDocumentsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<DocumentResponse>) {
                fetchRecentDocumentsResponse.postValue(Resource.success(o))
            }
        }
        fetchRecentDocumentsUseCase.execute(callback, FetchRecentDocumentsUseCase.Params.forDocs(0, 10, null))
    }
}