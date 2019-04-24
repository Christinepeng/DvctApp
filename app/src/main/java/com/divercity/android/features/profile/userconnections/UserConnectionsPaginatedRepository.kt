package com.divercity.android.features.profile.userconnections

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.userconnections.usecase.FetchFollowersUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class UserConnectionsPaginatedRepository @Inject
constructor(private val fetchFollowersUseCase: FetchFollowersUseCase) :
    BaseDataSourceRepository<UserResponse>() {

    override fun getUseCase(): UseCase<List<UserResponse>, Params> {
        return fetchFollowersUseCase
    }

    fun setUserId(userId: String) {
        fetchFollowersUseCase.userId = userId
    }
}
