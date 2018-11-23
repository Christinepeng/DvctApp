package com.divercity.app.features.dialogs.jobapply.usecase

import android.net.Uri
import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.repository.document.DocumentRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
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
) : UseCase<DataObject<DocumentResponse>, UploadDocumentUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<DataObject<DocumentResponse>> {
        return repository.uploadDocument(params.docName, params.uri)
    }

    class Params private constructor(
        val docName: String,
        val uri: Uri
    ) {

        companion object {

            fun forDoc(
                docName: String,
                uri: Uri
            ): Params {
                return Params(docName, uri)
            }
        }
    }
}
