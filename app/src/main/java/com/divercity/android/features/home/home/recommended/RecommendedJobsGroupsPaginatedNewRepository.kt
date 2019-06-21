package com.divercity.android.features.home.home.recommended

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.RecommendedItem
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class RecommendedJobsGroupsPaginatedNewRepository @Inject
internal constructor(private val fetchFeedRecommendedJobsGroupsNewUseCase: FetchFeedRecommendedJobsGroupsNewUseCase) :
    BaseDataSourceRepository<RecommendedItem>() {

    override fun getUseCase(): UseCase<List<RecommendedItem>, Params> = fetchFeedRecommendedJobsGroupsNewUseCase
}
