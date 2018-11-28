package com.divercity.app.repository.job

import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface JobRepository {

    fun fetchJobs(page: Int, size: Int, query : String?): Observable<List<JobResponse>>

    fun saveJob(jobId : String) : Observable<JobResponse>

    fun removeSavedJob(jobId : String) : Observable<JobResponse>

    fun fetchJobById(jobId : String) : Observable<JobResponse>

    fun fetchSavedJobs(page: Int, size: Int, query : String?): Observable<IncludedArray<JobResponse>>

    fun fetchMyJobApplications(page: Int, size: Int, query : String?): Observable<List<JobApplicationResponse>>

    fun fetchJobTypes(): Observable<List<JobTypeResponse>>

    fun postJob(body : JobBody): Observable<JobResponse>

    fun shareJob(jobId: String, body : JobShareGroupBody): Observable<JobResponse>

    fun fetchJobPostingsByUser(userId: String, page: Int, size: Int, query: String?): Observable<List<JobResponse>>

    fun applyJob(jobId: String,
                 userDocId: String,
                 coverLetter: String): Observable<JobApplicationResponse>
}