package com.divercity.android.features.onboarding.selectschool

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.school.SchoolResponse
import com.divercity.android.features.onboarding.selectschool.usecase.FetchSchoolUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SchoolPaginatedRepository @Inject
internal constructor(private val fetchSchoolUseCase: FetchSchoolUseCase) :
    BaseDataSourceRepository<SchoolResponse>() {

    override fun getUseCase(): UseCase<List<SchoolResponse>, Params> = fetchSchoolUseCase
}
