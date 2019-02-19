package com.divercity.android.features.apollo

import com.apollographql.apollo.ApolloQueryCall
import com.divercity.android.JobQuery
import com.divercity.android.core.base.ApolloUseCase
import javax.inject.Inject

/**
 * Created by lucas on 06/12/2018.
 */

class FetchJobUseCase @Inject
constructor(private val apolloRepository: ApolloRepository) :
        ApolloUseCase<JobQuery.Data, FetchJobUseCase.Params>() {

    override fun buildQuery(params: Params): ApolloQueryCall<JobQuery.Data> {
        return apolloRepository.getJob(params.jobId)
    }

    class Params private constructor(val jobId: String) {

        companion object {

            fun forJob(jobId: String): Params {
                return Params(jobId)
            }
        }
    }
}