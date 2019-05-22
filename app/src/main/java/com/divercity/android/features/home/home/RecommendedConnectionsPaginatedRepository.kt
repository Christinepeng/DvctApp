package com.divercity.android.features.home.home

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.user.allconnections.usecase.FetchConnectionsUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class RecommendedConnectionsPaginatedRepository @Inject
internal constructor(private val fetchConnectionsUseCase: FetchConnectionsUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> = fetchConnectionsUseCase
}
