package com.divercity.android.features.multipleuseraction.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.groupadmin.AddGroupAdminsBody
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AddGroupAdminUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<String, AddGroupAdminUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: AddGroupAdminUseCase.Params): Observable<String> {
        return repository.addGroupAdmins(
            AddGroupAdminsBody(
                groupOfInterestId = params.groupId,
                userId = params.userIds[0]
            )
        )
    }

    class Params private constructor(val groupId: String, val userIds: List<String>) {

        companion object {

            fun forAdmin(groupId: String, userIds: List<String>): AddGroupAdminUseCase.Params {
                return AddGroupAdminUseCase.Params(groupId, userIds)
            }
        }
    }
}
