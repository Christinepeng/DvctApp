package com.divercity.android.features.user.editpersonal

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
 * Created by lucas on 27/09/2018.
 */

class PersonalSettingsViewModel @Inject
constructor(
    private val context: Application,
    private val sessionRepository: SessionRepository,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : BaseViewModel() {

    val updateUserProfileResponse = SingleLiveEvent<Resource<User>>()
    var uploadDocumentResponse = SingleLiveEvent<Resource<DocumentResponse>>()

    fun getCurrentUser() : LiveData<User> {
        return sessionRepository.getUserDB()
    }

    fun getUserType() : String?{
        return sessionRepository.getUserType()
    }

    fun updateEthnicity(ethnicity: String?) {
        val user = UserProfileEntity()
        user.ethnicity = ethnicity
        updateUserProfile(user)
    }

    fun updateGender(gender: String?) {
        val user = UserProfileEntity()
        user.gender = gender
        updateUserProfile(user)
    }

    fun updateAgeRange(ageRange: String?) {
        val user = UserProfileEntity()
        user.ageRange = ageRange
        updateUserProfile(user)
    }

    fun updateLocation(location: LocationResponse) {
        val user = UserProfileEntity()
        user.city = location.attributes?.name
        user.country = location.attributes?.countryName
        updateUserProfile(user)
    }

    fun updateCompany(companyId : String?){
        val user = UserProfileEntity()
        user.jobEmployerId = companyId
        updateUserProfile(user)
    }

    fun updateUserProfile(user: UserProfileEntity) {
        updateUserProfileResponse.postValue(Resource.loading<User>(null))

        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                updateUserProfileResponse.postValue(Resource.error<User>(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                updateUserProfileResponse.postValue(
                    Resource.error<User>(
                        error.toString(),
                        null
                    )
                )
            }

            override fun onSuccess(o: User) {
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
