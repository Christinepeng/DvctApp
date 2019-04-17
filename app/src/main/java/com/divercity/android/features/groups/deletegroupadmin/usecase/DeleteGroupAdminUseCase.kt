package com.divercity.android.features.groups.deletegroupadmin.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteGroupAdminUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<Unit, DeleteGroupAdminUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Unit> {
        return repository.deleteGroupAdmins(params.adminsId)
    }

    class Params constructor(val adminsId: List<String>)
}
