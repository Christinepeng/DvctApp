package com.divercity.android.features.groups.createeditgroup.step1.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteGroupUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<Boolean, DeleteGroupUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        return repository.deleteGroup(params.groupId)
    }

    class Params private constructor(val groupId: String) {

        companion object {

            fun forGroups(groupId: String): Params {
                return Params(groupId)
            }
        }
    }
}
