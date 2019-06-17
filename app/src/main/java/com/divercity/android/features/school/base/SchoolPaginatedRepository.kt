package com.divercity.android.features.school.base

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.school.base.usecase.FetchSchoolUseCase
import com.divercity.android.model.School
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SchoolPaginatedRepository @Inject
internal constructor(private val fetchSchoolUseCase: FetchSchoolUseCase) :
    BaseDataSourceRepository<School>() {

    override fun getUseCase(): UseCase<List<School>, Params> = fetchSchoolUseCase
}
