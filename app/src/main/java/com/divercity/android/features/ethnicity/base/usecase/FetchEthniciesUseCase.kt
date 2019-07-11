package com.divercity.android.features.ethnicity.base.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.Ethnicity
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchEthniciesUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DataRepository
) : UseCase<@JvmSuppressWildcards List<Ethnicity>, Unit?>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Unit?): Observable<List<Ethnicity>> {
        return repository.fetchEthnicites()
    }
}
