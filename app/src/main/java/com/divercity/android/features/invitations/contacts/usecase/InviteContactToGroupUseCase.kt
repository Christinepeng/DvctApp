package com.divercity.android.features.invitations.contacts.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContact
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class InviteContactToGroupUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<GroupInviteResponse, InviteContactToGroupUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<GroupInviteResponse> {
        return repository.inviteContact(params.invite)
    }

    class Params private constructor(val invite : GroupInviteContact) {

        companion object {

            fun forGroups(invite : GroupInviteContact): Params {
                return Params(
                    invite
                )
            }
        }
    }
}
