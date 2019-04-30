package com.divercity.android.features.groups.createtopic.usecase

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

class CreateTopicUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<Question, CreateTopicUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Question> {
        return repository.createNewTopic(params.question, params.groupId, params.image)
    }

    class Params constructor(val question: String, val groupId: String, val image: String?)
}
