package com.divercity.android.features.groups.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class RequestJoinGroupUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: GroupRepository
) : UseCase<MessageResponse, RequestJoinGroupUseCase.Params>(
        executorThread,
        uiThread) {

    override fun createObservableUseCase(params: Params): Observable<MessageResponse> {
        return repository.requestToJoinGroup(params.groupId)
    }

    class Params private constructor(val groupId: String) {

        companion object {

            fun toJoin(groupId: String): Params {
                return Params(groupId)
            }
        }
    }
}
