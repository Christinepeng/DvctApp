package com.divercity.app.features.groups.groupdetail.about.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchGroupAdminsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<List<UserResponse>, FetchGroupAdminsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: FetchGroupAdminsUseCase.Params): Observable<List<UserResponse>> {
        return repository.fetchGroupAdmins(params.groupId!!, params.page, params.size, params.query)
    }

    class Params private constructor(val groupId: String?, val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forGroups(groupId: String?, page: Int, size: Int, query: String?): FetchGroupAdminsUseCase.Params {
                return FetchGroupAdminsUseCase.Params(groupId, page, size, query)
            }
        }
    }
}
