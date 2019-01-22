package com.divercity.android.features.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.divercity.android.FindQuery
import javax.inject.Inject

/**
 * Created by lucas on 05/12/2018.
 */

class ApolloRepository @Inject
constructor(private val apolloClient: ApolloClient) {

    fun getRepositoryData(reponame : String, username : String) : ApolloQueryCall<FindQuery.Data>{

        return apolloClient.query(FindQuery.builder()
            .name(reponame)
            .owner(username)
            .build())
    }
}