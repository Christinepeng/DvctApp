package com.divercity.android.features.groups.all.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.repository.group.GroupRepository
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
) : UseCase<List<GroupResponse>, Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<GroupResponse>> {
        return repository.fetchAllGroups(
            params.page,
            params.size,
            params.query
        )
    }
}
