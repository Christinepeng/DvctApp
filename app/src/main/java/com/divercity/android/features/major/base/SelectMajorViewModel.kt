package com.divercity.android.features.major.base

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.model.Major
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectMajorViewModel @Inject
constructor(
    repository: MajorPaginatedRepository
) : BaseViewModelPagination<Major>(repository) {

    init {
        fetchData()
    }
}
