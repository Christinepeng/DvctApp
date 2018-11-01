package com.divercity.app.repository.job

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.job.JobResponse
import com.divercity.app.data.networking.services.JobService
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class JobRepositoryImpl
@Inject constructor(private val jobService: JobService) : JobRepository {

    override fun fetchJobs(page: Int, size: Int): Observable<DataArray<JobResponse>> {
        return jobService.fetchJobs(page, size)
    }
}