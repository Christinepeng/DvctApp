package com.divercity.app.core.base

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.divercity.app.core.functional.Either
import kotlinx.coroutines.*
import kotlin.coroutines.resume

/**
 * Created by lucas on 07/12/2018.
 */

abstract class ApolloUseCase<T, Params> {

    protected lateinit var call: ApolloQueryCall<T>
    protected lateinit var job: Job

    protected abstract fun buildQuery(params: Params): ApolloQueryCall<T>

    operator fun invoke(params: Params, onResult: (Either<String, T>) -> Unit = {}) {
        call = buildQuery(params)
        job = GlobalScope.launch(Dispatchers.Main) {

            onResult(suspendCancellableCoroutine { continuation ->

                val callback = object : ApolloCall.Callback<T>() {

                    override fun onFailure(e: ApolloException) {
                        if (continuation.isActive)
                            continuation.resume(Either.Left(e.cause?.message ?: "Server error"))
                    }

                    override fun onResponse(response: Response<T>) {
                        if (continuation.isActive)
                            if (response.hasErrors())
                                continuation.resume(Either.Left("Has errors"))
                            else
                                continuation.resume(Either.Right(response.data()!!))
                    }
                }
                call.enqueue(callback)
            })
        }
    }

    fun cancel() {
        job.cancel()
        call.cancel()
    }
}