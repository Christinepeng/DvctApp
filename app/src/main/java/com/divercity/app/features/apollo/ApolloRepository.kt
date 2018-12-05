package com.divercity.app.features.apollo

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.divercity.app.FindQuery
import javax.inject.Inject

/**
 * Created by lucas on 05/12/2018.
 */

class ApolloRepository @Inject
constructor(private val apolloClient: ApolloClient) {

    fun getRepositoryData(reponame : String, username : String){
        apolloClient.query(FindQuery.builder()
            .name(reponame)
            .owner(username)
            .build())
            .enqueue(object : ApolloCall.Callback<FindQuery.Data>() {

                override fun onFailure(e: ApolloException) {

                }

                override fun onResponse(response: Response<FindQuery.Data>) {

                }
            })
    }
}