package com.divercity.android.features.home.home

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.features.home.home.recommended.old.RecommendedJobsGroupsPaginatedRepository
import javax.inject.Inject

class HomeRecommendedViewModel @Inject
constructor(
    repository: RecommendedJobsGroupsPaginatedRepository
) : BaseViewModelPagination<RecommendedItem>(repository) {

    init {
        fetchData()
    }
}
