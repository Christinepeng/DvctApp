package com.divercity.android.features.groups.groupdetail.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchGroupMembersUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<List<UserResponse>, FetchGroupMembersUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: FetchGroupMembersUseCase.Params): Observable<List<UserResponse>> {
        return repository.fetchGroupMembers(params.groupId!!, params.page, params.size, params.query)
    }

    class Params private constructor(val groupId: String?, val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forGroups(groupId: String?, page: Int, size: Int, query: String?): FetchGroupMembersUseCase.Params {
                return FetchGroupMembersUseCase.Params(groupId, page, size, query)
            }
        }
    }
}
