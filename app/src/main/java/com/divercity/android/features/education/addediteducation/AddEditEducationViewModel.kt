package com.divercity.android.features.education.addediteducation

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.education.addediteducation.usecase.AddEducationUseCase
import com.divercity.android.features.education.addediteducation.usecase.EditEducationUseCase
import com.divercity.android.model.Education
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class AddEditEducationViewModel @Inject
constructor(
    private val addEducationUseCase: AddEducationUseCase,
    private val editEducationUseCase: EditEducationUseCase
) : BaseViewModel() {

    var addEditEducationResponse = SingleLiveEvent<Resource<Education>>()

    fun addEducation(
        schoolId: String,
        major: String,
        from: String,
        to: String,
        degreeId: String
    ) {
        addEditEducationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Education>() {
            override fun onFail(error: String) {
                addEditEducationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addEditEducationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Education) {
                addEditEducationResponse.postValue(Resource.success(o))
            }
        }
        addEducationUseCase.execute(
            callback, AddEducationUseCase.Params.toAdd(schoolId, major, from, to, degreeId)
        )
    }

    fun editEducation(
        educationId: String,
        schoolId: String?,
        major: String?,
        from: String?,
        to: String?,
        degreeId: String?
    ) {
        addEditEducationResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Education>() {
            override fun onFail(error: String) {
                addEditEducationResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addEditEducationResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Education) {
                addEditEducationResponse.postValue(Resource.success(o))
            }
        }
        editEducationUseCase.execute(
            callback, EditEducationUseCase.Params.toEdit(educationId, schoolId, major, from, to, degreeId)
        )
    }

    override fun onCleared() {
        super.onCleared()
        addEducationUseCase.dispose()
    }
}