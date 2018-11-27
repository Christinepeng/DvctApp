package com.divercity.app.features.dialogs.jobapply

import android.app.Application
import android.net.Uri
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.FileUtils
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 23/11/2018.
 */

class JobApplyDialogViewModel @Inject
constructor(private val context: Application,
            private val uploadDocumentUseCase: UploadDocumentUseCase) : BaseViewModel() {

    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()

    fun checkDocumentAndUploadIt(uri: Uri) {
        val mimeType = FileUtils.getMimeType(context, uri)
        val file: File?
        if (mimeType == null) {
            file = File(uri.path)
            val fileType = FileUtils.getFileExtension(file)
            if (fileType.contains("pdf")) {
                uploadDocument(file)
            } else {
                uploadDocumentResponse.postValue(Resource.error(context.getString(R.string.select_valid_file), null))
            }
        } else if (mimeType.contains("pdf")) {
            file = FileUtils.getFileFromContentResolver(context, uri)
            if (file != null)
                uploadDocument(file)
            else
                uploadDocumentResponse.postValue(Resource.error(context.getString(R.string.select_valid_file), null))
        } else {
            uploadDocumentResponse.postValue(Resource.error(context.getString(R.string.select_valid_file), null))
        }
    }

    private fun uploadDocument(file: File) {
        uploadDocumentResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<DocumentResponse>() {
            override fun onFail(error: String) {
                uploadDocumentResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                uploadDocumentResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: DocumentResponse) {
                uploadDocumentResponse.postValue(Resource.success(o))
            }
        }
        uploadDocumentUseCase.execute(callback, UploadDocumentUseCase.Params.forDoc(file))
    }
}