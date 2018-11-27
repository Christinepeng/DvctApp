package com.divercity.app.features.dialogs.jobapply.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.repository.document.DocumentRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.io.File
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class ApplyToJobUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DocumentRepository
) : UseCase<DocumentResponse, ApplyToJobUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DocumentResponse> {
        return repository.uploadDocument(params.file)
    }

    class Params private constructor(
            val file: File
            ) {

        companion object {

            fun forDoc(
                file: File
            ): Params {
                return Params(file)
            }
        }
    }
}
