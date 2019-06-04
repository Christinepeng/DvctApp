package com.divercity.android.features.onboarding.selectoccupationofinterests

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.occupationofinterests.OOIResponse
import com.divercity.android.features.onboarding.selectoccupationofinterests.usecase.FetchOOIUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class OOIPaginatedRepository @Inject
internal constructor(private val fetchOOIUseCase: FetchOOIUseCase) :
    BaseDataSourceRepository<OOIResponse>() {

    override fun getUseCase(): UseCase<List<OOIResponse>, Params> = fetchOOIUseCase
}
