package com.divercity.android.features.home.people.connections.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
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

        val fetchRecommendedUsers = userRepository.fetchRecommendedUsers(
            0,
            5,
            if (params.query == "") null else params.query
        )

        val fetchConnectionRequests = userRepository.fetchConnectionRequests(
            sessionRepository.getUserId(),
            0,
            5,
            null
        )

        val fetchUsers = userRepository.fetchUsers(
            params.page,
            params.size,
            if (params.query == "") null else params.query
        )

        return Observable.zip(
            fetchRecommendedUsers,
            fetchConnectionRequests,
            fetchUsers,
            Function3 { t1, t2, t3 ->
                val connections = ArrayList<UserResponse>()
                connections.addAll(t1)
                connections.addAll(t2)
                connections.addAll(t3)
                return@Function3 connections
            }
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