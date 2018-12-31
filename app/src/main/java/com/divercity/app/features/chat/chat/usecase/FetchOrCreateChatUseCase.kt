package com.divercity.app.features.chat.chat.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.createchat.CreateChatResponse
import com.divercity.app.repository.chat.ChatRepository
import com.divercity.app.repository.user.UserRepository
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
        private val repository: UserRepository,
        private val chatRepository: ChatRepository
) : UseCase<@JvmSuppressWildcards CreateChatResponse, FetchOrCreateChatUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<CreateChatResponse> {
        return chatRepository.createChat(repository.getUserId()!!, params.otherUserId)
    }

    class Params private constructor(val otherUserId: String) {

        companion object {

            fun forUser(otherUserId: String): Params {
                return Params(otherUserId)
            }
        }
    }
}
