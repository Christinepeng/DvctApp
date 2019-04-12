package com.divercity.android.features.jobs.jobs

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.jobs.jobs.usecase.FetchJobsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class JobPaginatedRepository @Inject
internal constructor(private val fetchJobsUseCase: FetchJobsUseCase) :
    BaseDataSourceRepository<JobResponse>() {

    override fun getUseCase(): UseCase<List<JobResponse>, Params> = fetchJobsUseCase
}
