package com.divercity.android.features.groups.yourgroups

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.groups.yourgroups.usecase.FetchPopularGroupQuestionsUseCase
import com.divercity.android.model.Question
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class PopularGroupQuestionsPaginatedRepository @Inject
internal constructor(private val fetchPopularGroupQuestionsUseCase: FetchPopularGroupQuestionsUseCase) :
    BaseDataSourceRepository<Question>() {

    override fun getUseCase(): UseCase<List<Question>, Params> = fetchPopularGroupQuestionsUseCase
}
