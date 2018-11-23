package com.divercity.app.features.dialogs.jobapply

import android.net.Uri
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 23/11/2018.
 */

class JobApplyDialogViewModel @Inject
constructor(private val uploadDocumentUseCase: UploadDocumentUseCase) : BaseViewModel() {

    fun uploadDocument(strName : String, fileUri : Uri){
        val callback = object : DisposableObserverWrapper<DataObject<DocumentResponse>>() {
            override fun onFail(error: String) {
            }

            override fun onHttpException(error: JsonElement) {
            }

            override fun onSuccess(o: DataObject<DocumentResponse>) {
            }
        }
        uploadDocumentUseCase.execute(callback, UploadDocumentUseCase.Params.forDoc(strName, fileUri))
    }
}