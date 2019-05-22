package com.divercity.android.features.groups.followedgroups

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.selectfollowedgroup.FollowedGroupsPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class FollowedGroupsViewModel @Inject
constructor(
    repository: FollowedGroupsPaginatedRepository
) : BaseViewModelPagination<GroupResponse>(repository) {

    init {
        fetchData()
    }
}
