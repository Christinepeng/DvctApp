package com.divercity.android.features.groups.selectfollowedgroup

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.jobs.jobposting.sharetogroup.usecase.FetchFollowedGroupsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class FollowedGroupsPaginatedRepository @Inject
internal constructor(private val fetchFollowedGroupsUseCase: FetchFollowedGroupsUseCase) :
    BaseDataSourceRepository<GroupResponse>() {

    override fun getUseCase(): UseCase<List<GroupResponse>, Params> = fetchFollowedGroupsUseCase
}
