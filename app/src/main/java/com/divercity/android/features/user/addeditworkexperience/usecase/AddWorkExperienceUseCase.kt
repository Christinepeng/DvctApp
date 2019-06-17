package com.divercity.android.features.user.addeditworkexperience.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.workexperience.body.Experience
import com.divercity.android.data.entity.workexperience.body.WorkExperienceBody
import com.divercity.android.model.WorkExperience
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
) : UseCase<WorkExperience, AddWorkExperienceUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<WorkExperience> {
        return repository.addExperience(WorkExperienceBody(params.experience))
    }

    class Params private constructor(val experience: Experience) {

        companion object {

            fun to(experience: Experience): Params {
                return Params(experience)
            }
        }
    }
}
