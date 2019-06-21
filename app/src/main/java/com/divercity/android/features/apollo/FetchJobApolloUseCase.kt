package com.divercity.android.features.apollo

import android.view.View
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.divercity.android.JobQuery
import com.divercity.android.core.functional.Either
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Created by lucas on 06/12/2018.
 */

class FetchJobApolloUseCase @Inject
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
                                -1,
                                null,
                                e.cause?.message ?: "Server error",
                                params.view
                            )
                            continuation.resume(Either.Left(r))
                        }
                    }

                    override fun onResponse(response: Response<JobQuery.Data>) {
                        if (continuation.isActive)
                            if (response.hasErrors()) {
                                val r = JobDataView(
                                    -2,
                                    null,
                                    "Error getting job",
                                    params.view
                                )
                                continuation.resume(Either.Left(r))
                            } else {
                                val r = JobDataView(
                                    response.data()!!.Job().id().toInt(),
                                    response.data()!!,
                                    null,
                                    params.view
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

    private fun buildQuery(params: Params): ApolloQueryCall<JobQuery.Data> {
        return apolloRepository.getJob(params.chatMessageResponse.embeddedAttachmentId!!)
    }

    class Params private constructor(
        val chatMessageResponse: ChatMessageEntityResponse,
        val view: View?
    ) {

        companion object {

            fun forJob(
                chatMessageResponse: ChatMessageEntityResponse,
                view: View?
            ): Params {
                return Params(chatMessageResponse, view)
            }
        }
    }

    data class JobDataView(
        var id: Int,
        var job: JobQuery.Data?,
        var errors: String?,
        var view: View?
    )
}