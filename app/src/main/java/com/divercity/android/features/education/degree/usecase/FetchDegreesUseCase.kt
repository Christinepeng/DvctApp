package com.divercity.android.features.education.degree.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.Degree
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchDegreesUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DataRepository
) : UseCase<@JvmSuppressWildcards List<Degree>, Unit?>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Unit?): Observable<List<Degree>> {
        return repository.fetchDegrees()
    }
}
