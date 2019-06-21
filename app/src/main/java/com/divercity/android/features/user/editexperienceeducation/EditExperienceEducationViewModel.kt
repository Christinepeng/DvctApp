package com.divercity.android.features.user.editexperienceeducation

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.education.addediteducation.usecase.DeleteEducationUseCase
import com.divercity.android.features.user.addeditworkexperience.usecase.DeleteWorkExperienceUseCase
import com.divercity.android.features.user.usecase.FetchEducationsUseCase
import com.divercity.android.features.user.usecase.FetchWorkExperiencesUseCase
import com.divercity.android.model.Education
import com.divercity.android.model.WorkExperience
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class EditExperienceEducationViewModel @Inject
constructor(
    private val fetchWorkExperiencesUseCase: FetchWorkExperiencesUseCase,
    private val sessionRepository: SessionRepository,
    private val fetchEducationsUseCase: FetchEducationsUseCase,
    private val deleteEducationUseCase: DeleteEducationUseCase,
    private val deleteWorkExperienceUseCase: DeleteWorkExperienceUseCase
) : BaseViewModel() {

    val fetchWorkExperiencesResponse = MutableLiveData<Resource<List<WorkExperience>>>()
    val fetchEducationsResponse = MutableLiveData<Resource<List<Education>>>()
    val deleteEducationResponse = MutableLiveData<Resource<Unit>>()
    val deleteWorkExperienceResponse = MutableLiveData<Resource<Unit>>()

    init {
        fetchWorkExperiences()
        fetchEducations()
    }

    fun fetchWorkExperiences() {
        fetchWorkExperiencesResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<WorkExperience>>() {
            override fun onFail(error: String) {
                fetchWorkExperiencesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchWorkExperiencesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<WorkExperience>) {
                fetchWorkExperiencesResponse.postValue(Resource.success(o))
            }
        }
        fetchWorkExperiencesUseCase.execute(
            callback,
            FetchWorkExperiencesUseCase.Params.to(sessionRepository.getUserId())
        )
    }

    fun fetchEducations() {
        fetchEducationsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<Education>>() {
            override fun onFail(error: String) {
                fetchEducationsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchEducationsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<Education>) {
                fetchEducationsResponse.postValue(Resource.success(o))
            }
        }
        fetchEducationsUseCase.execute(
            callback,
            FetchEducationsUseCase.Params.to(sessionRepository.getUserId())
        )
    }

    fun deleteEducation(
        educationId: String
    ) {
        deleteEducationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                deleteEducationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteEducationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                deleteEducationResponse.postValue(Resource.success(o))
            }
        }
        deleteEducationUseCase.execute(
            callback, DeleteEducationUseCase.Params.toDelete(educationId)
        )
    }

    fun deleteWorkExperience(
        expId: String
    ) {
        deleteWorkExperienceResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                deleteWorkExperienceResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteWorkExperienceResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                deleteWorkExperienceResponse.postValue(Resource.success(o))
            }
        }
        deleteWorkExperienceUseCase.execute(
            callback, DeleteWorkExperienceUseCase.Params.to(expId)
        )
    }
}
