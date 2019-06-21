package com.divercity.android.features.education.addediteducation.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.Education
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AddEducationUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Education, AddEducationUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Education> {

        return repository.addEducation(
            params.schoolId,
            params.major,
            params.from,
            params.to,
            params.degreeId
        )
    }

    class Params private constructor(
        val schoolId: String,
        val major: String,
        val from: String,
        val to: String,
        val degreeId: String
    ) {

        companion object {

            fun toAdd(
                schoolId: String,
                major: String,
                from: String,
                to: String,
                degreeId: String
            ): Params {
                return Params(
                    schoolId,
                    major,
                    from,
                    to,
                    degreeId
                )
            }
        }
    }
}
