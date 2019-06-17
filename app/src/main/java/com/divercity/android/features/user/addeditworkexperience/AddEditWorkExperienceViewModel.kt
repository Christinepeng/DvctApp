package com.divercity.android.features.user.addeditworkexperience

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.workexperience.body.Experience
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.user.addeditworkexperience.usecase.AddWorkExperienceUseCase
import com.divercity.android.features.user.addeditworkexperience.usecase.EditWorkExperienceUseCase
import com.divercity.android.model.WorkExperience
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class AddEditWorkExperienceViewModel @Inject
constructor(
    private val addWorkExperienceUseCase: AddWorkExperienceUseCase,
    private val editWorkExperienceUseCase: EditWorkExperienceUseCase
) : BaseViewModel() {

    var addEditWorkExperienceResponse = SingleLiveEvent<Resource<WorkExperience>>()

    fun addWorkExperience(
        companyId: String,
        jobTitle: String,
        startDate: String,
        endDate: String?,
        isCurrentWorking: Boolean?,
        description: String
    ) {
        addEditWorkExperienceResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<WorkExperience>() {
            override fun onFail(error: String) {
                addEditWorkExperienceResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addEditWorkExperienceResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: WorkExperience) {
                addEditWorkExperienceResponse.postValue(Resource.success(o))
            }
        }
        addWorkExperienceUseCase.execute(
            callback, AddWorkExperienceUseCase.Params.to(
                Experience(
                    jobEmployerId = companyId,
                    role = jobTitle,
                    jobStart = startDate,
                    jobEnd = endDate,
                    isPresent = isCurrentWorking,
                    experienceDetails = description
                )
            )
        )
    }

    fun editExperience(
        expId: String,
        companyId: String?,
        jobTitle: String,
        startDate: String,
        endDate: String?,
        isCurrentWorking: Boolean?,
        description: String
    ) {
        addEditWorkExperienceResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<WorkExperience>() {
            override fun onFail(error: String) {
                addEditWorkExperienceResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addEditWorkExperienceResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: WorkExperience) {
                addEditWorkExperienceResponse.postValue(Resource.success(o))
            }
        }
        editWorkExperienceUseCase.execute(
            callback, EditWorkExperienceUseCase.Params.to(
                expId,
                Experience(
                    jobEmployerId = companyId,
                    role = jobTitle,
                    jobStart = startDate,
                    jobEnd = endDate,
                    isPresent = isCurrentWorking,
                    experienceDetails = description
                )
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        addWorkExperienceUseCase.dispose()
    }
}