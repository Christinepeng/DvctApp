package com.divercity.android.features.home.people.connections.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.usecase.FetchUsersUseCase
import com.divercity.android.features.home.people.connections.usecase.FetchConnectionsUseCase

class ConnectionsSourceFactory(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val fetchConnectionsUseCase: FetchConnectionsUseCase,
    private val query: String
) : DataSource.Factory<Long, UserResponse>() {

    val connectionsDataSource = MutableLiveData<ConnectionsDataSource>()

    override fun create(): DataSource<Long, UserResponse> {

        val followedGroupsDataSource = ConnectionsDataSource(
            fetchConnectionsUseCase,
            fetchUsersUseCase,
            query
        )

        connectionsDataSource.postValue(followedGroupsDataSource)
        return followedGroupsDataSource
    }

}
