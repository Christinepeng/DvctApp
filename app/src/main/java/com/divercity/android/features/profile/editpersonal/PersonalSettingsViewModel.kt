package com.divercity.android.features.profile.editpersonal

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import com.divercity.android.R
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.FileUtils
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.profile.profile.User
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.dialogs.jobapply.usecase.UploadDocumentUseCase
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import java.io.File
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class PersonalSettingsViewModel @Inject
constructor(
    private val context: Application,
    private val sessionRepository: SessionRepository,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : BaseViewModel() {

    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()
    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()

    fun getCurrentUser() : LiveData<UserResponse> {
        return sessionRepository.getUserDB()
    }

    fun getUserType() : String?{
        return sessionRepository.getUserType()
    }

    fun updateEthnicity(ethnicity: String?) {
        val user = User()
        user.ethnicity = ethnicity
        updateUserProfile(user)
    }

    fun updateGender(gender: String?) {
        val user = User()
        user.gender = gender
        updateUserProfile(user)
    }

    fun updateAgeRange(ageRange: String?) {
        val user = User()
        user.ageRange = ageRange
        updateUserProfile(user)
    }

    fun updateLocation(location: LocationResponse) {
        val user = User()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfile(user)
    }

    fun updateCompany(companyId : String?){
        val user = User()
        user.jobEmployerId = companyId
        updateUserProfile(user)
    }

    fun updateUserProfile(user: User) {
        updateUserProfileResponse.postValue(Resource.loading<UserResponse>(null))

        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<UserResponse>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(
                    Resource.error<UserResponse>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: UserResponse) {
                updateUserProfileResponse.postValue(Resource.success(o))
            }
        }
        updateUserProfileUseCase.execute(callback, UpdateUserProfileUseCase.Params.forUser(user))
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
}
