package com.divercity.android.features.chat.chat.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.messages.DataChatMessageResponse
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchMessagesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: ChatRepository,
            private val sessionRepository: SessionRepository
) : UseCase<@JvmSuppressWildcards DataChatMessageResponse, FetchMessagesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DataChatMessageResponse> {
        return repository.fetchMessages(
                sessionRepository.getUserId(),
                params.chatId,
                params.otherUserId,
                params.page,
                params.size,
                if (params.query != null && params.query == "") null else params.query)
    }

    class Params private constructor(val chatId: String, val otherUserId: String,
                                     val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forMsgs(chatId: String, otherUserId: String, page: Int, size: Int, query: String?): Params {
                return Params(chatId, otherUserId, page, size, query)
            }
        }
    }
}
