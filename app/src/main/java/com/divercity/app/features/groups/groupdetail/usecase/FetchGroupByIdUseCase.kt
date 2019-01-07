package com.divercity.app.features.groups.groupdetail.usecase

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

class FetchGroupByIdUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<DataArray<GroupResponse>, FetchGroupByIdUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: FetchGroupByIdUseCase.Params): Observable<DataArray<GroupResponse>> {
        return repository.fetchMyGroups(params.page, params.size, params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forGroups(page: Int, size: Int, query: String?): FetchGroupByIdUseCase.Params {
                return FetchGroupByIdUseCase.Params(page, size, query)
            }
        }
    }
}
