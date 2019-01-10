package com.divercity.app.features.groups.creategroup.step3.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.group.creategroup.GroupOfInterest
import com.divercity.app.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class CreateGroupUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<GroupResponse, CreateGroupUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<GroupResponse> {
        return repository.createGroup(params.group)
    }

    class Params private constructor(val group : GroupOfInterest) {

        companion object {

            fun forGroups(group : GroupOfInterest): Params {
                return Params(group)
            }
        }
    }
}
