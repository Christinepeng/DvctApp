package com.divercity.android.features.user.allconnections

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.chat.usecase.FetchUsersUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class UsersPaginatedRepository @Inject
internal constructor(private val fetchUsersUseCase: FetchUsersUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> = fetchUsersUseCase
}
