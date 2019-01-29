package com.divercity.android.features.chat.chat.usecase

import com.divercity.android.core.base.UseCase
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

class FetchOrCreateChatUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val sessionRepository: SessionRepository,
        private val chatRepository: ChatRepository
) : UseCase<@JvmSuppressWildcards CreateChatResponse, FetchOrCreateChatUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<CreateChatResponse> {
        return chatRepository.createChat(sessionRepository.getUserId(), params.otherUserId)
    }

    class Params private constructor(val otherUserId: String) {

        companion object {

            fun forUser(otherUserId: String): Params {
                return Params(otherUserId)
            }
        }
    }
}
