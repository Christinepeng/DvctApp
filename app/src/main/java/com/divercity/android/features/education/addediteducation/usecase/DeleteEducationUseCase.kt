package com.divercity.android.features.education.addediteducation.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteEducationUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Unit, DeleteEducationUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {

        return repository.deleteEducation(
            params.educationId
        )
    }

    class Params private constructor(
        val educationId: String
    ) {

        companion object {

            fun toDelete(
                educationId: String
            ): Params {
                return Params(
                    educationId
                )
            }
        }
    }
}
