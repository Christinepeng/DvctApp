package com.divercity.android.features.dialogs.recentdocuments

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.dialogs.recentdocuments.usecase.FetchRecentDocumentsUseCase
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