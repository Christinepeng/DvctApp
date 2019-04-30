package com.divercity.android.features.groups.groupdetail.conversation.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.Question
import com.divercity.android.repository.group.GroupRepository
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
) : UseCase<@JvmSuppressWildcards List<Question>, Params>(executorThread, uiThread) {

    lateinit var groupId: String

    override fun createObservableUseCase(params: Params): Observable<List<Question>> {
        return repository.fetchQuestions(groupId, params.page, params.size, params.query)
    }
}
