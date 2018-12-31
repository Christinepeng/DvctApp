package com.divercity.app.features.dialogs.jobapplication

import android.app.Application
import android.net.Uri
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.FileUtils
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.data.Resource
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import com.divercity.app.data.networking.config.DisposableObserverWrapper
import com.divercity.app.features.dialogs.jobapplication.usecase.CancelJobApplicationUseCase
import com.divercity.app.features.dialogs.jobapplication.usecase.EditJobApplicationUseCase
import com.divercity.app.features.dialogs.jobapplication.usecase.FetchJobApplicationUseCase
import com.divercity.app.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 23/11/2018.
 */

class JobApplicationDialogViewModel @Inject
constructor(private val context: Application,
            private val fetchJobApplicationUseCase : FetchJobApplicationUseCase,
            private val uploadDocumentUseCase: UploadDocumentUseCase,
            private val cancelJobApplicationUseCase: CancelJobApplicationUseCase,
            private val editJobApplicationUseCase: EditJobApplicationUseCase) : BaseViewModel() {

    var fetchJobApplicationResponse = SingleLiveEvent<Resource<JobApplicationResponse>>()
    var editJobApplicationResponse = SingleLiveEvent<Resource<JobApplicationResponse>>()
    var cancelJobApplicationResponse = SingleLiveEvent<Resource<JobApplicationResponse>>()
    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()

    fun fetchJobApplication(jobId : String) {
        fetchJobApplicationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobApplicationResponse>() {
            override fun onFail(error: String) {
                fetchJobApplicationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchJobApplicationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobApplicationResponse) {
                fetchJobApplicationResponse.postValue(Resource.success(o))
            }
        }
        fetchJobApplicationUseCase.execute(callback, FetchJobApplicationUseCase.Params.forApplication(jobId))
    }

    fun editJobApplication(jobId: String, userDocId: String, coverLetter: String) {
        editJobApplicationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobApplicationResponse>() {
            override fun onFail(error: String) {
                editJobApplicationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                editJobApplicationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobApplicationResponse) {
                editJobApplicationResponse.postValue(Resource.success(o))
            }
        }
        editJobApplicationUseCase.execute(callback, EditJobApplicationUseCase.Params.toEdit(jobId,userDocId, coverLetter))
    }

    fun cancelJobApplication(jobId: String) {
        cancelJobApplicationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<JobApplicationResponse>() {
            override fun onFail(error: String) {
                cancelJobApplicationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                cancelJobApplicationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: JobApplicationResponse) {
                cancelJobApplicationResponse.postValue(Resource.success(o))
            }
        }
        cancelJobApplicationUseCase.execute(callback, CancelJobApplicationUseCase.Params.toCancel(jobId))
    }

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