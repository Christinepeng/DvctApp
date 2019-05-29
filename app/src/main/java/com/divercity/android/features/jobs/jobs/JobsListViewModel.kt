package com.divercity.android.features.jobs.jobs

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.job.response.JobResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobsListViewModel @Inject
constructor(
    repository: JobPaginatedRepository
) : BaseViewModelPagination<JobResponse>(repository) {

    var jobSaveUnsaveResponse = SingleLiveEvent<Resource<JobResponse>>()

    /* Filters */
    var viewTypeFilter = "All"
    var locationFilter = "All"
    var companyFilter = "All Sizes, All"

    init {
        fetchData()
    }
}
