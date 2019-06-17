package com.divercity.android.features.school.base.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.School
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchSchoolUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: DataRepository
) : UseCase<@JvmSuppressWildcards List<School>, Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<School>> {
        return repository.fetchSchool(
                params.page,
                params.size,
                params.query)
    }
}
