package com.divercity.android.features.skill.base

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.data.entity.user.response.UserEntityResponse
import com.divercity.android.repository.paginated.SkillPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectSkillViewModel @Inject
constructor(repository: SkillPaginatedRepository
) : BaseViewModelPagination<SkillResponse>(repository) {

    val updateUserProfileResponse = SingleLiveEvent<Resource<UserEntityResponse>>()

    init {
        fetchData(null, "")
    }
}
