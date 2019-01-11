package com.divercity.app.features.chat.chat.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.repository.chat.ChatRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class SendMessagesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: ChatRepository)
    : UseCase<ChatMessageResponse, SendMessagesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<ChatMessageResponse> {
        return repository.sendMessage(
                params.message,
                params.chatId)
    }

    class Params private constructor(val message: String, val chatId: String) {

        companion object {

            fun forMsg(message: String, chatId: String): Params {
                return Params(message, chatId)
            }
        }
    }
}