package com.divercity.android.features.apollo

import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.divercity.android.JobQuery
import com.divercity.android.core.functional.Either
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Created by lucas on 06/12/2018.
 */

class FetchJobReloadedUseCase @Inject
constructor(private val apolloRepository: ApolloRepository) {

    private var calls = ArrayList<ApolloQueryCall<JobQuery.Data>>()
    private var scopes = ArrayList<CoroutineScope>()

    operator fun invoke(params: Params, onResult: (Either<JobDataView, JobDataView>) -> Unit = {}) {

        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Main + job)

        scopes.add(scope)
        scope.launch {

            onResult(suspendCancellableCoroutine { continuation ->

                val callback = object : ApolloCall.Callback<JobQuery.Data>() {

                    override fun onFailure(e: ApolloException) {
                        if (continuation.isActive) {
                            val r = JobDataView(
                                null,
                                e.cause?.message ?: "Server error",
                                params.position
                            )
                            continuation.resume(Either.Left(r))
                        }
                    }

                    override fun onResponse(response: Response<JobQuery.Data>) {
                        if (continuation.isActive)
                            if (response.hasErrors()) {
                                val r = JobDataView(
                                    null,
                                    "Error getting company",
                                    params.position
                                )
                                continuation.resume(Either.Left(r))
                            } else {
                                val r = JobDataView(
                                    response.data()!!,
                                    null,
                                    params.position
                                )
                                continuation.resume(Either.Right(r))
                            }
                    }
                }
                val call = buildQuery(params)
                calls.add(call)
                call.enqueue(callback)
            })
        }
    }

    fun cancel() {
        calls.forEach { it.cancel() }
        scopes.forEach { it.coroutineContext.cancelChildren() }
    }

    fun buildQuery(params: Params): ApolloQueryCall<JobQuery.Data> {
        return apolloRepository.getJob(params.jobId)
    }

    class Params private constructor(
        val jobId: String,
        val position: Int,
        val view: View,
        val chatMessageResponse: ChatMessageResponse
    ) {

        companion object {

            fun forJob(
                jobId: String,
                position: Int,
                view: View,
                chatMessageResponse: ChatMessageResponse
            ): Params {
                return Params(jobId, position, view, chatMessageResponse)
            }
        }
    }

    data class JobDataView(var job: JobQuery.Data?, var errors: String?, var position: Int)
}