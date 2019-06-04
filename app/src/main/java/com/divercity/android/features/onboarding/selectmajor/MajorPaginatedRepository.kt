package com.divercity.android.features.onboarding.selectmajor

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.major.MajorResponse
import com.divercity.android.features.onboarding.selectmajor.usecase.FetchMajorsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class MajorPaginatedRepository @Inject
internal constructor(private val fetchMajorsUseCase: FetchMajorsUseCase) :
    BaseDataSourceRepository<MajorResponse>() {

    override fun getUseCase(): UseCase<List<MajorResponse>, Params> = fetchMajorsUseCase
}
