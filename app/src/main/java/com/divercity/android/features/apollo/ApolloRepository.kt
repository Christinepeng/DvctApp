package com.divercity.android.features.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.divercity.android.JobQuery
import javax.inject.Inject

/**
 * Created by lucas on 05/12/2018.
 */

class ApolloRepository @Inject
constructor(private val apolloClient: ApolloClient) {

    fun getJob(jobId : String) : ApolloQueryCall<JobQuery.Data> {

        return apolloClient.query(JobQuery.builder()
            .id(jobId)
            .build())
    }
}