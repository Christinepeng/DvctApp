package com.divercity.android.features.groups.groupdetail.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchGroupByIdUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<GroupResponse, FetchGroupByIdUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<GroupResponse> {
        return repository.fetchGroupById(params.groupId)
    }

    class Params private constructor(val groupId: String) {

        companion object {

            fun forGroups(groupId: String) : Params{
                return Params(groupId)
            }
        }
    }
}
