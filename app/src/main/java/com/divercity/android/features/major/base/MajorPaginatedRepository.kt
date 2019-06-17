package com.divercity.android.features.major.base

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.major.base.usecase.FetchMajorsUseCase
import com.divercity.android.model.Major
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class MajorPaginatedRepository @Inject
internal constructor(private val fetchMajorsUseCase: FetchMajorsUseCase) :
    BaseDataSourceRepository<Major>() {

    override fun getUseCase(): UseCase<List<Major>, Params> = fetchMajorsUseCase
}
