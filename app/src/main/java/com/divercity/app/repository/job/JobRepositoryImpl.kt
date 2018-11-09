package com.divercity.app.repository.job

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.networking.services.JobService
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class JobRepositoryImpl
@Inject constructor(private val jobService: JobService) : JobRepository {

    override fun shareJob(jobId: String, body: JobShareGroupBody): Observable<JobResponse> {
        return jobService.shareJob(jobId, body).map {
            it.data
        }
    }

    override fun postJob(body: JobBody): Observable<DataObject<JobResponse>> {
        return jobService.postJob(body)
    }

    override fun fetchSavedJobs(page: Int, size: Int, query: String?): Observable<IncludedArray<JobResponse>> {
        return jobService.fetchSavedJobs(page, size, query)
    }

    override fun saveJob(jobId: String): Observable<JobResponse> {
        return jobService.saveJob(jobId).map {
            it.data
        }
    }

    override fun removeSavedJob(jobId: String): Observable<JobResponse> {
        return jobService.removeSavedJob(jobId).map {
            it.data
        }
    }

    override fun fetchJobs(page: Int, size: Int, query: String?): Observable<DataArray<JobResponse>> {
        return jobService.fetchJobs(page, size, query)
    }

    override fun fetchJobTypes(): Observable<List<JobTypeResponse>> {
        return jobService.fetchJobTypes().map {
            it.data
        }
    }
}