package com.divercity.android.features.jobs.savedjobs

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.job.response.JobResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SavedJobsViewModel @Inject
constructor(
    repository: SavedJobPaginatedRepository
) : BaseViewModelPagination<JobResponse>(repository) {

    init {
        fetchData(null, "")
    }
}
