package com.divercity.android.features.groups.allgroups

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.allgroups.usecase.FetchAllGroupsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class AllGroupsPaginatedRepository @Inject
internal constructor(private val fetchGroupsUseCase: FetchAllGroupsUseCase) :
    BaseDataSourceRepository<GroupResponse>() {

    override fun getUseCase(): UseCase<List<GroupResponse>, Params> {
        return fetchGroupsUseCase
    }
}
