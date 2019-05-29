package com.divercity.android.features.location.base

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.features.location.base.usecase.FetchLocationsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class LocationPaginatedRepository @Inject
internal constructor(private val fetchLocationsUseCase: FetchLocationsUseCase) :
    BaseDataSourceRepository<LocationResponse>() {

    override fun getUseCase(): UseCase<List<LocationResponse>, Params> = fetchLocationsUseCase
}
