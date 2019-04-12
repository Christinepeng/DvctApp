package com.divercity.android.features.chat.creategroupchat.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.creategroupchatbody.CreateGroupChatBody
import com.divercity.android.data.entity.createchat.CreateChatResponse
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class CreateGroupChatUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val sessionRepository: SessionRepository,
    private val chatRepository: ChatRepository
) : UseCase<@JvmSuppressWildcards CreateChatResponse, CreateGroupChatUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<CreateChatResponse> {
        return chatRepository.createGroupChat(
            sessionRepository.getUserId(),
            params.otherUserId,
            params.createGroupChatBody
        )
    }

    class Params private constructor(
        val otherUserId: String,
        val createGroupChatBody: CreateGroupChatBody
    ) {

        companion object {

            fun forUser(otherUserId: String, createGroupChatBody: CreateGroupChatBody): Params {
                return Params(otherUserId, createGroupChatBody)
            }
        }
    }
}
