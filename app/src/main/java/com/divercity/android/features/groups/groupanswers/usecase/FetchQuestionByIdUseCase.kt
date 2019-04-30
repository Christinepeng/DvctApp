package com.divercity.android.features.groups.groupanswers.usecase

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

class FetchQuestionByIdUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: GroupRepository
) : UseCase<Question, FetchQuestionByIdUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Question> {
        return repository.fetchQuestionById(params.questionId)
    }

    class Params constructor(val questionId: String)
}
