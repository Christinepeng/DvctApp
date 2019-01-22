package com.divercity.android.features.company.companysize.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchCompanySizesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: DataRepository
) : UseCase<List<CompanySizeResponse>, Any?>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Any?): Observable<List<CompanySizeResponse>> {
        return repository.fetchCompanySizes()
    }
}
