package com.divercity.android.features.groups.deletegroupadmin

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.groups.groupdetail.about.usecase.FetchGroupAdminsUseCase
import com.divercity.android.model.user.User
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class GroupAdminsPaginatedRepository @Inject
internal constructor(private val fetchGroupAdminsUseCase: FetchGroupAdminsUseCase) :
    BaseDataSourceRepository<User>() {

    override fun getUseCase(): UseCase<List<User>, Params> = fetchGroupAdminsUseCase

    fun setGroupId(groupId: String) {
        fetchGroupAdminsUseCase.groupId = groupId
    }
}
