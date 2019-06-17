package com.divercity.android.features.school.base

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.model.School
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectSchoolViewModel @Inject
constructor(
    repository: SchoolPaginatedRepository
) : BaseViewModelPagination<School>(repository) {

    init {
        fetchData()
    }
}
