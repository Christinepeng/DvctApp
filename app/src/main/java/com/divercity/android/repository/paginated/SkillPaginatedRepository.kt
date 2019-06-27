package com.divercity.android.repository.paginated

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.skill.base.usecase.FetchSkillsUseCase
import com.divercity.android.testing.OpenForTesting
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

@OpenForTesting
class SkillPaginatedRepository @Inject
internal constructor(private val fetchSkillsUseCase: FetchSkillsUseCase) :
    BaseDataSourceRepository<SkillResponse>() {

    override fun getUseCase(): UseCase<List<SkillResponse>, Params> = fetchSkillsUseCase
}
