package com.divercity.android.features.onboarding.professionalInfo

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.FileUtils
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.Resource.Companion.error
import com.divercity.android.data.Resource.Companion.loading
import com.divercity.android.data.Resource.Companion.success
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */
class ProfessionalInfoViewModel @Inject
constructor(
    private val context: Application,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    var updateUserProfileUseCase: UpdateUserProfileUseCase,
    var sessionRepository: SessionRepository
) : BaseViewModel() {

    var dataUpdateUser = MutableLiveData<Resource<User>>()
    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()

    val accountType: String
        get() = sessionRepository.getAccountType()

    fun updateUserProfile(typeId: String?) {
        dataUpdateUser.postValue(loading<User>(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                dataUpdateUser.postValue(
                    error<User>(
                        error,
                        null
                    )
                )
            }

            override fun onHttpException(error: JsonElement) {
                dataUpdateUser.postValue(
                    error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: User) {
                dataUpdateUser.postValue(
                    success(
                        o
                    )
                )
            }
        }
        val user = UserProfileEntity()
        user.accountType = typeId
        updateUserProfileUseCase.execute(
            callback,
            UpdateUserProfileUseCase.Params(user)
        )
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
                uploadDocumentResponse.postValue(
                    Resource.error(
                        context.getString(R.string.select_valid_file),
                        null
                    )
                )
            }
        } else if (mimeType.contains("pdf")) {
            file = FileUtils.getFileFromContentResolver(context, uri)
            if (file != null)
                uploadDocument(file)
            else
                uploadDocumentResponse.postValue(
                    Resource.error(
                        context.getString(R.string.select_valid_file),
                        null
                    )
                )
        } else {
            uploadDocumentResponse.postValue(
                Resource.error(
                    context.getString(R.string.select_valid_file),
                    null
                )
            )
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

    override fun onCleared() {
        super.onCleared()
        updateUserProfileUseCase.dispose()
    }

}