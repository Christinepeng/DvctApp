package com.divercity.android.features.home.home.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchUnreadMessagesCountUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository,
    private val sessionRepository: SessionRepository
) : UseCase<Int, Any?>(executorThread, uiThread) {

    override fun createObservableUseCase(a: Any?): Observable<Int> {
        return repository.fetchUnreadMessagesCount(sessionRepository.getUserId())
    }
}
