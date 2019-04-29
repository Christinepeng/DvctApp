package com.divercity.android.features.user.experience

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.workexperience.body.Experience
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.user.experience.usecase.AddWorkExperienceUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 05/11/2018.
 */

class AddWorkExperienceViewModel @Inject
constructor(
    private val addWorkExperienceUseCase: AddWorkExperienceUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    var addWorkExperienceResponse = SingleLiveEvent<Resource<WorkExperienceResponse>>()

    fun addWorkExperience(
        companyId: String,
        jobTitle: String,
        startDate: String,
        endDate: String?,
        isCurrentWorking: Boolean?,
        description: String
    ) {
        addWorkExperienceResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<WorkExperienceResponse>() {
            override fun onFail(error: String) {
                addWorkExperienceResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addWorkExperienceResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: WorkExperienceResponse) {
                addWorkExperienceResponse.postValue(Resource.success(o))
            }
        }
        addWorkExperienceUseCase.execute(
            callback, AddWorkExperienceUseCase.Params.to(
                sessionRepository.getUserId(),
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