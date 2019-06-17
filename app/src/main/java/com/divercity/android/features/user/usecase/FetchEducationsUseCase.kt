package com.divercity.android.features.user.usecase

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

class FetchEducationsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: UserRepository
) : UseCase<@JvmSuppressWildcards List<Education>, FetchEducationsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<Education>> {
        return repository.fetchEducations(params.userId)
    }

    class Params private constructor(val userId: String) {

        companion object {

            fun to(userId: String): Params {
                return Params(userId)
            }
        }
    }
}
