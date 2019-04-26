package com.divercity.android.features.chat.chat.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.repository.chat.ChatRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class SendMessagesUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: ChatRepository
) : UseCase<ChatMessageEntityResponse, SendMessagesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<ChatMessageEntityResponse> {
        return repository.sendMessage(
            params.message,
            params.chatId,
            params.image
//            ,
//            params.attchmntType,
//            params.attachmntId
        )
    }

    class Params private constructor(
        val message: String,
        val chatId: String,
        val image: String
//        ,
//        val attchmntType: String,
//        val attachmntId: String
    ) {

        companion object {

            fun forMsg(message: String,
                       chatId: String,
                       image: String
//                       ,
//                       attchmntType: String,
//                       attachmntId: String
            ): Params {
                return Params(
                    message,
                    chatId,
                    image
//                    ,
//                    attchmntType,
//                    attachmntId
                )
            }
        }
    }
}
