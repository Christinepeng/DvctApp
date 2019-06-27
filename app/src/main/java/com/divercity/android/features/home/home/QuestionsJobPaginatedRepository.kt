package com.divercity.android.features.home.home

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.features.home.home.usecase.FetchQuestionsJobsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class QuestionsJobPaginatedRepository @Inject
internal constructor(private val fetchQuestionsJobsUseCase: FetchQuestionsJobsUseCase) :
    BaseDataSourceRepository<HomeItem>() {

    init {
        pageSize = 10
    }

    override fun getUseCase(): UseCase<List<HomeItem>, Params> = fetchQuestionsJobsUseCase
}
