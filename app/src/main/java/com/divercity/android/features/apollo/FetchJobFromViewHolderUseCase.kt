package com.divercity.android.features.apollo

import com.apollographql.apollo.ApolloQueryCall
import com.divercity.android.core.base.usecase.ApolloSuspendUseCase
import javax.inject.Inject

/**
 * Created by lucas on 06/12/2018.
 */

class FetchJobFromViewHolderUseCase @Inject
constructor(private val apolloRepository: ApolloRepository) :
    ApolloSuspendUseCase<JobQuery.Data, FetchJobFromViewHolderUseCase.Params>() {

    override fun buildQuery(params: Params): ApolloQueryCall<JobQuery.Data> {
        return apolloRepository.getJob(params.jobId)
    }

    class Params private constructor(
        val jobId: String
    ) {

        companion object {

            fun forJob(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}