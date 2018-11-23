package com.divercity.app.features.groups.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.group.recommendedgroups.RecommendedGroupsResponse
import com.divercity.app.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchRecommendedGroupsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DataRepository
) : UseCase<DataObject<RecommendedGroupsResponse>, Any>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Any): Observable<DataObject<RecommendedGroupsResponse>> {
        return repository.fetchRecommendedGroups()
    }
}
