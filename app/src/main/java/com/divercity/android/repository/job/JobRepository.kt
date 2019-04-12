package com.divercity.android.repository.job

import com.divercity.android.data.entity.job.jobpostingbody.JobBody
import com.divercity.android.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.android.data.entity.job.jobtype.JobTypeResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface JobRepository {

    fun fetchRecommendedJobs(page: Int, size: Int, query: String?): Observable<List<JobResponse>>

    fun fetchJobs(page: Int, size: Int, query: String?): Observable<List<JobResponse>>

    fun fetchSimilarJobs(
        pageNumber: Int,
        size: Int,
        jobId: String?
    ): Observable<List<JobResponse>>

    fun saveJob(jobId: String): Observable<JobResponse>

    fun removeSavedJob(jobId: String): Observable<JobResponse>

    fun fetchJobById(jobId: String): Observable<JobResponse>

    fun fetchSavedJobs(page: Int, size: Int, query: String?): Observable<List<JobResponse>>

    fun fetchMyJobApplications(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<JobApplicationResponse>>

    fun fetchJobTypes(): Observable<List<JobTypeResponse>>

    fun postJob(body: JobBody): Observable<JobResponse>

    fun shareJob(jobId: String, body: JobShareGroupBody): Observable<JobResponse>

    fun fetchJobPostingsByUser(
        userId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<JobResponse>>

    fun applyJob(
        jobId: String,
        userDocId: String,
        coverLetter: String
    ): Observable<JobApplicationResponse>

    fun editJobApplication(
        jobId: String,
        userDocId: String,
        coverLetter: String
    ): Observable<JobApplicationResponse>

    fun fetchApplicants(
        jobId: String,
        pageNumber: Int,
        size: Int
    ): Observable<List<JobApplicationResponse>>

    fun performActionJob(
        jobId: String,
        action: String
    ): Observable<JobResponse>

    fun editJob(jobId: String, body: JobBody): Observable<JobResponse>

    fun cancelJobApplication(jobId: String): Observable<JobApplicationResponse>

    fun fetchJobApplication(applicationId: String): Observable<JobApplicationResponse>

    fun fetchJobsByCompany(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<JobResponse>>
}