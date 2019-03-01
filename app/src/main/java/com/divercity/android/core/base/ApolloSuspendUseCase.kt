package com.divercity.android.core.base

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.divercity.android.core.functional.Either
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by lucas on 07/12/2018.
 */

abstract class ApolloSuspendUseCase<T, Params> {

    private var calls = ArrayList<ApolloQueryCall<T>>()

    protected abstract fun buildQuery(params: Params): ApolloQueryCall<T>

    operator suspend fun invoke(params: Params, onResult: (Either<String, T>) -> Unit = {}) {
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
            val call = buildQuery(params)
            calls.add(call)
            call.enqueue(callback)
        })
    }

    fun cancel() {
        calls.forEach { it.cancel() }
    }
}