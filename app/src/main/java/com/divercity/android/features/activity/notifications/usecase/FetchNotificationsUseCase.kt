package com.divercity.android.features.activity.notifications.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchNotificationsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<@JvmSuppressWildcards List<NotificationResponse>, FetchNotificationsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<NotificationResponse>> {
        return repository.fetchNotifications(params.page, params.size)
    }

    class Params private constructor(val page: Int, val size: Int) {

        companion object {

            fun forFollowing(page: Int, size: Int): Params {
                return Params(page, size)
            }
        }
    }
}
