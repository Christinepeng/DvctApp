package com.divercity.app.features.groups.groupdetail.conversation.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.questions.QuestionResponse
import com.divercity.app.repository.group.GroupRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchGroupConversationsCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: GroupRepository
) : UseCase<@JvmSuppressWildcards List<QuestionResponse>, FetchGroupConversationsCase.Params>(executorThread, uiThread) {

    lateinit var groupId: String

    override fun createObservableUseCase(params: Params): Observable<List<QuestionResponse>> {
        return repository.fetchQuestions(groupId, params.page, params.size, params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forQuestions(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
