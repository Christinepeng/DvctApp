package com.divercity.android.features.picturessearch.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import com.divercity.android.repository.data.DataRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class SearchPicturesUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: DataRepository
) : UseCase<@JvmSuppressWildcards List<PhotoEntityResponse>, SearchPicturesUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<PhotoEntityResponse>> {
        return repository.searchPhotos(params.query)
    }

    class Params private constructor(val query: String) {

        companion object {

            fun toSearch(query: String): Params {
                return Params(query)
            }
        }
    }
}
