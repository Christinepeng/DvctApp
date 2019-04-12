package com.divercity.android.features.activity.notifications.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class MarkNotificationReadUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<Unit, MarkNotificationReadUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.markNotificationRead(params.notifId)
    }

    class Params private constructor(val notifId: String) {

        companion object {

            fun toMark(notifId: String): Params {
                return Params(notifId)
            }
        }
    }
}
