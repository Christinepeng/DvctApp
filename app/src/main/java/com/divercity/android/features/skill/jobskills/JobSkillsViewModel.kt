package com.divercity.android.features.skill.jobskills

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.repository.paginated.SkillPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobSkillsViewModel @Inject
constructor(repository: SkillPaginatedRepository) :
    BaseViewModelPagination<SkillResponse>(repository) {

    init {
        fetchData(null,"")
    }
}
