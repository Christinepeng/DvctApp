package com.divercity.android.features.user.addeditworkexperience.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteWorkExperienceUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Unit, DeleteWorkExperienceUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.deleteExperience(params.expId)
    }

    class Params private constructor(val expId: String) {

        companion object {

            fun to(expId: String): Params {
                return Params(expId)
            }
        }
    }
}
