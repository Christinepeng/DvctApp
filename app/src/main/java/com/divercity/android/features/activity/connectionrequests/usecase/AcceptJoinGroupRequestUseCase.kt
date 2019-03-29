package com.divercity.android.features.activity.connectionrequests.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AcceptJoinGroupRequestUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<Unit, AcceptJoinGroupRequestUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.acceptJoinGroupRequest(params.groupId, params.userId)
    }

    class Params private constructor(
        val groupId: String,
        val userId: String
    ) {

        companion object {

            fun toAccept(
                groupId: String,
                userId: String
            ): Params {
                return Params(groupId, userId)
            }
        }
    }
}
