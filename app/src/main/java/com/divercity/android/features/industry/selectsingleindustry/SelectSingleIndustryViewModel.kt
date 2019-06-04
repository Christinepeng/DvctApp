package com.divercity.android.features.industry.selectsingleindustry

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.features.industry.base.IndustryPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectSingleIndustryViewModel @Inject
constructor(repository: IndustryPaginatedRepository) :
    BaseViewModelPagination<IndustryResponse>(repository) {

    init {
        fetchData()
    }
}
