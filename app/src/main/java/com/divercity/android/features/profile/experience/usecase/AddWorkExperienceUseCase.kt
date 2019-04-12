package com.divercity.android.features.profile.experience.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.workexperience.body.Experience
import com.divercity.android.data.entity.workexperience.body.WorkExperienceBody
import com.divercity.android.data.entity.workexperience.response.WorkExperienceResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AddWorkExperienceUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<WorkExperienceResponse, AddWorkExperienceUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<WorkExperienceResponse> {
        return repository.addNewExperience(params.userId, WorkExperienceBody(params.experience))
    }

    class Params private constructor(val userId: String, val experience: Experience) {

        companion object {

            fun to(userId: String, experience: Experience): Params {
                return Params(userId, experience)
            }
        }
    }
}
