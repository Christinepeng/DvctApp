package com.divercity.android.features.groups.groupanswers.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse
import com.divercity.android.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchAnswersUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<List<AnswerEntityResponse>, FetchAnswersUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<AnswerEntityResponse>> {
        return repository.fetchAnswers(
            params.questionId,
            params.page,
            params.size,
            if (params.query == "") null else params.query
        )
    }

    class Params private constructor(
        val questionId: String,
        val page: Int,
        val size: Int,
        val query: String
    ) {

        companion object {

            fun forAnswers(questionId: String, page: Int, size: Int, query: String): Params {
                return Params(questionId, page, size, query)
            }
        }
    }
}
