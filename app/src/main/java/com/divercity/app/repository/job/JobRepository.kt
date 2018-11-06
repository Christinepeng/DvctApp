package com.divercity.app.repository.job

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.JobResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface JobRepository {

    fun fetchJobs(page: Int, size: Int, query : String?): Observable<DataArray<JobResponse>>

    fun saveJob(jobId : String) : Observable<JobResponse>

    fun removeSavedJob(jobId : String) : Observable<JobResponse>

    fun fetchSavedJobs(page: Int, size: Int, query : String?): Observable<IncludedArray<JobResponse>>
}