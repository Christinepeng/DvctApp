package com.divercity.app.features.dialogs.recentdocuments.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.repository.document.DocumentRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchRecentDocumentsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: DocumentRepository
) : UseCase<List<DocumentResponse>, FetchRecentDocumentsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<DocumentResponse>> {
        return repository.fetchRecentDocs()
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forDocs(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
