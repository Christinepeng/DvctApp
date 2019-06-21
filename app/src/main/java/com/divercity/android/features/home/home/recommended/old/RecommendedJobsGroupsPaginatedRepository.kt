package com.divercity.android.features.home.home.recommended.old

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.RecommendedItem
import com.divercity.android.features.home.home.usecase.FetchFeedRecommendedJobsGroupsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class RecommendedJobsGroupsPaginatedRepository @Inject
internal constructor(private val fetchFeedRecommendedJobsGroupsUseCase: FetchFeedRecommendedJobsGroupsUseCase) :
    BaseDataSourceRepository<RecommendedItem>() {

    override fun getUseCase(): UseCase<List<RecommendedItem>, Params> = fetchFeedRecommendedJobsGroupsUseCase
}
