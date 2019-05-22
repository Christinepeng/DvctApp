package com.divercity.android.features.groups.deletegroupmember

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.groups.groupdetail.usecase.FetchGroupMembersUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class GroupMembersPaginatedRepository @Inject
internal constructor(private val fetchGroupMembersUseCase: FetchGroupMembersUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> = fetchGroupMembersUseCase

    fun setGroupId(groupId: String) {
        fetchGroupMembersUseCase.groupId = groupId
    }
}
