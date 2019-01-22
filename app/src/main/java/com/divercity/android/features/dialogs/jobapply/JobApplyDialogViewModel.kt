package com.divercity.android.features.dialogs.jobapply

import android.app.Application
import android.net.Uri
import com.divercity.android.R
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.FileUtils
import com.divercity.android.core.utils.NetworkErrorsUtil
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.dialogs.jobapply.usecase.ApplyToJobUseCase
import com.divercity.android.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 23/11/2018.
 */

class JobApplyDialogViewModel @Inject
constructor(private val context: Application,
            private val uploadDocumentUseCase: UploadDocumentUseCase,
            private val applyToJobUseCase: ApplyToJobUseCase) : BaseViewModel() {

    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()
    var applyToJobResponse = SingleLiveEvent<Resource<JobApplicationResponse>>()

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

    fun applyToJob(jobId: String, userDocId: String, coverLetter: String) {
        applyToJobResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobApplicationResponse>() {
            override fun onFail(error: String) {
                applyToJobResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                applyToJobResponse.postValue(Resource.error(NetworkErrorsUtil.getErrorNode(error), null))
            }

            override fun onSuccess(o: JobApplicationResponse) {
                applyToJobResponse.postValue(Resource.success(o))
            }
        }
        applyToJobUseCase.execute(callback, ApplyToJobUseCase.Params.forJob(jobId, userDocId, coverLetter))
    }
}