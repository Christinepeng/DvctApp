package com.divercity.android.features.jobs.jobposting.skills.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchSkillsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: DataRepository
) : UseCase<DataArray<SkillResponse>, FetchSkillsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DataArray<SkillResponse>> {
        return repository.fetchSkills(params.page, params.size, params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forSkills(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
