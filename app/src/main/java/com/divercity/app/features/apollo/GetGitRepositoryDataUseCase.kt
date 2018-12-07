package com.divercity.app.features.apollo

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.divercity.app.FindQuery
import com.divercity.app.core.base.ApolloUseCase
import javax.inject.Inject

/**
 * Created by lucas on 06/12/2018.
 */

class GetGitRepositoryDataUseCase @Inject
constructor(private val apolloClient: ApolloClient) :
        ApolloUseCase<FindQuery.Data, GetGitRepositoryDataUseCase.Params>() {

    override fun buildQuery(params: Params): ApolloQueryCall<FindQuery.Data> {
        return apolloClient.query(
                FindQuery.builder()
                        .name(params.reponame)
                        .owner(params.username)
                        .build()
        )
    }

    class Params private constructor(val reponame: String, val username: String) {

        companion object {

            fun forRepo(reponame: String, username: String): Params {
                return Params(reponame, username)
            }
        }
    }
}