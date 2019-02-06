package com.divercity.android.features.chat.creategroupchat.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class AddChatGroupMemberUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val sessionRepository: SessionRepository,
    private val chatRepository: ChatRepository
) : UseCase<Boolean, AddChatGroupMemberUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        return chatRepository.addGroupMember(
            sessionRepository.getUserId(),
            params.chatId,
            params.usersIds
        )
    }

    class Params private constructor(
        val chatId: String,
        val usersIds : List<String>
    ) {

        companion object {

            fun forUser(chatId: String, usersIds : List<String>): Params {
                return Params(chatId, usersIds)
            }
        }
    }
}
