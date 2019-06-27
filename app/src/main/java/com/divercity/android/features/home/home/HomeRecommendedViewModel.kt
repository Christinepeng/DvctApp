package com.divercity.android.features.home.home

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.features.home.home.recommended.RecommendedJobsGroupsPaginatedNewRepository
import javax.inject.Inject

class HomeRecommendedViewModel @Inject
constructor(
    repository: RecommendedJobsGroupsPaginatedNewRepository
) : BaseViewModelPagination<RecommendedItem>(repository) {

    init {
        fetchData()
    }
}
