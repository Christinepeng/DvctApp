package com.divercity.android.features.home.people.connections.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.session.SessionRepository
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
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : UseCase<@JvmSuppressWildcards List<UserResponse>, FetchConnectionsUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<List<UserResponse>> {

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