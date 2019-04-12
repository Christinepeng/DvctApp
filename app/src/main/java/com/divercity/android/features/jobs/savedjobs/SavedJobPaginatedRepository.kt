package com.divercity.android.features.jobs.savedjobs

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.savedjobs.usecase.FetchSavedJobsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class SavedJobPaginatedRepository @Inject
internal constructor(private val fetchJobsUseCase: FetchSavedJobsUseCase) :
    BaseDataSourceRepository<JobResponse>() {

    override fun getUseCase(): UseCase<List<JobResponse>, Params> = fetchJobsUseCase
}
