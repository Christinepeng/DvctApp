package com.divercity.android.features.groups.deletegroupmember.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.message.MessagesResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class DeleteGroupMemberUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<MessagesResponse, DeleteGroupMemberUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<MessagesResponse> {
        return repository.deleteGroupMembers(params.groupId, params.memberIds)
    }

    class Params constructor(val groupId: String, val memberIds: List<String>)
}
