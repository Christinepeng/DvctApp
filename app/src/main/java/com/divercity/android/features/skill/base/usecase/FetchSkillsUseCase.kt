package com.divercity.android.features.skill.base.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.repository.data.DataRepository
import com.divercity.android.testing.OpenForTesting
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

@OpenForTesting
class FetchSkillsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DataRepository
) : UseCase<List<SkillResponse>, Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<SkillResponse>> {
        return repository.fetchSkills(
            params.page,
            params.size,
            params.query
        )
    }
}
