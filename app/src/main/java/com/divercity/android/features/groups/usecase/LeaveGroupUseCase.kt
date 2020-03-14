package com.divercity.android.features.groups.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class LeaveGroupUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: GroupRepository
) : UseCase<Boolean, LeaveGroupUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        return repository.leaveGroup(params.groupId)
    }

    class Params private constructor(val groupId: String) {

        companion object {

            fun leave(groupId: String): Params {
                return Params(groupId)
            }
        }
    }
}
