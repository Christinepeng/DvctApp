package com.divercity.android.features.groups.mygroups

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.mygroups.usecase.FetchMyGroupsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class MyGroupsPaginatedRepository @Inject
internal constructor(private val fetchMyGroupsUseCase: FetchMyGroupsUseCase) :
    BaseDataSourceRepository<GroupResponse>() {

    override fun getUseCase(): UseCase<List<GroupResponse>, Params> = fetchMyGroupsUseCase
}
