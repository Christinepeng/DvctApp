package com.divercity.android.features.user.allconnections.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchConnectionsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userRepository: UserRepository
) : UseCase<@JvmSuppressWildcards List<User>, FetchConnectionsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<User>> {

        return userRepository.fetchRecommendedUsers(
            params.page,
            params.size,
            if (params.query == "") null else params.query
        )
    }

    class Params private constructor(val page: Int, val size: Int, val query: String) {

        companion object {

            fun forJobs(page: Int, size: Int, query: String): Params {
                return Params(page, size, query)
            }
        }
    }
}