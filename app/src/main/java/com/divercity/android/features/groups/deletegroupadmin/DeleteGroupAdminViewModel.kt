package com.divercity.android.features.groups.deletegroupadmin

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.groups.deletegroupadmin.usecase.DeleteGroupAdminUseCase
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupAdminViewModel @Inject
constructor(
    repository: GroupAdminsPaginatedRepository,
    deleteGroupAdminUseCase: DeleteGroupAdminUseCase
) : BaseViewModelPagination<UserResponse>(repository) {

    var groupId: String = ""
        set(value) {
            (repository as GroupAdminsPaginatedRepository).setGroupId(value)
        }
}
