package com.divercity.android.features.location.base

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.location.LocationResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectLocationViewModel @Inject
constructor(repository: LocationPaginatedRepository) :
    BaseViewModelPagination<LocationResponse>(repository) {

    init {
        fetchData()
    }
}
