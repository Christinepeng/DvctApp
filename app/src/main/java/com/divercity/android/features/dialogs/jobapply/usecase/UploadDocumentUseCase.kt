package com.divercity.android.features.dialogs.jobapply.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.repository.document.DocumentRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.io.File
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UploadDocumentUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DocumentRepository
) : UseCase<DocumentResponse, UploadDocumentUseCase.Params>(executorThread, uiThread) {

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
