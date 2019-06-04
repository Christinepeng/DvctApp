package com.divercity.android.features.industry.base

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.features.industry.base.usecase.FetchIndustriesUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class IndustryPaginatedRepository @Inject
internal constructor(private val fetchIndustriesUseCase: FetchIndustriesUseCase) :
    BaseDataSourceRepository<IndustryResponse>() {

    override fun getUseCase(): UseCase<List<IndustryResponse>, Params> = fetchIndustriesUseCase
}
