package com.divercity.android.features.singleuseraction.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ShareJobViaMessageUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository
) : UseCase<ChatMessageEntityResponse, ShareJobViaMessageUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<ChatMessageEntityResponse> {

        return chatRepository.createChat(sessionRepository.getUserId(), params.otherUserId)
            .flatMap(Function {
                chatRepository.sendMessageAttachment(
                    "Sharing a job",
                    it.id,
                    "Job",
                    params.jobId
                )
            })
    }

    class Params private constructor(val otherUserId: String, val jobId: String) {

        companion object {

            fun forJob(otherUserId: String, jobId: String): Params {
                return Params(otherUserId, jobId)
            }
        }
    }
}