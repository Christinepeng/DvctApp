package com.divercity.android.features.jobs.jobposting.sharetogroup.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchFollowedGroupsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<DataArray<GroupResponse>, FetchFollowedGroupsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DataArray<GroupResponse>> {
        return repository.fetchFollowedGroups(
            params.page,
            params.size,
            if(params.query == "") null else params.query
        )
    }

    class Params private constructor(
        val page: Int,
        val size: Int,
        val query: String
    ) {
        companion object {

            fun forGroups(page: Int, size: Int, query: String): Params {
                return Params(page, size, query)
            }
        }
    }
}
