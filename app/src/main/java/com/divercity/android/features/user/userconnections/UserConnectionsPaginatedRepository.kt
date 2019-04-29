package com.divercity.android.features.user.userconnections

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.user.userconnections.usecase.FetchFollowersUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class UserConnectionsPaginatedRepository @Inject
constructor(private val fetchFollowersUseCase: FetchFollowersUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> {
        return fetchFollowersUseCase
    }

    fun setUserId(userId: String) {
        fetchFollowersUseCase.userId = userId
    }
}
