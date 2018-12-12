package com.divercity.app.features.onboarding.selectinterests.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.interests.InterestsResponse
import com.divercity.app.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchInterestsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: DataRepository
) : UseCase<@JvmSuppressWildcards List<InterestsResponse>, Any?>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Any?): Observable<List<InterestsResponse>> {
        return repository.fetchInterests()
    }
}
