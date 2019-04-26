package com.divercity.android.features.home.people.connections.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.features.chat.usecase.FetchUsersUseCase
import com.divercity.android.features.home.people.connections.usecase.FetchConnectionsUseCase
import com.divercity.android.model.user.User

class ConnectionsSourceFactory(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val fetchConnectionsUseCase: FetchConnectionsUseCase,
    private val query: String
) : DataSource.Factory<Long, User>() {

    val connectionsDataSource = MutableLiveData<ConnectionsDataSource>()

    override fun create(): DataSource<Long, User> {

        val followedGroupsDataSource = ConnectionsDataSource(
            fetchConnectionsUseCase,
            fetchUsersUseCase,
            query
        )

        connectionsDataSource.postValue(followedGroupsDataSource)
        return followedGroupsDataSource
    }

}
