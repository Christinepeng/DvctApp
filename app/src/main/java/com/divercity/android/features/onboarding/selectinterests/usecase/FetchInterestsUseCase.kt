package com.divercity.android.features.onboarding.selectinterests.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.repository.data.DataRepository
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
