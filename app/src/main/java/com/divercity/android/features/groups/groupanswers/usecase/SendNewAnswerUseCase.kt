package com.divercity.android.features.groups.groupanswers.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.answer.body.Answer
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class SendNewAnswerUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<AnswerEntityResponse, SendNewAnswerUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<AnswerEntityResponse> {
        return repository.sendNewAnswer(
            AnswerBody(
                Answer(
                    params.images,
                    params.text,
                    params.questionId
                )
            )
        )
    }

    class Params private constructor(
        val images: List<String>?,
        val text: String,
        val questionId: String
    ) {

        companion object {

            fun forAnswer(
                images: List<String>?,
                text: String,
                questionId: String
            ): Params {
                return Params(images, text, questionId)
            }
        }
    }
}
