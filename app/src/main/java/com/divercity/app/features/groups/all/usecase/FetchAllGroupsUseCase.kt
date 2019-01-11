package com.divercity.app.features.groups.all.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchAllGroupsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<DataArray<GroupResponse>, FetchAllGroupsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: FetchAllGroupsUseCase.Params): Observable<DataArray<GroupResponse>> {
        return repository.fetchTrendingGroups(
            params.page,
            params.size,
            if (params.query == "") null else params.query
        )
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forGroups(page: Int, size: Int, query: String?): FetchAllGroupsUseCase.Params {
                return FetchAllGroupsUseCase.Params(page, size, query)
            }
        }
    }
}