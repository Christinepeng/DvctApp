package com.divercity.app.repository.job

import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import com.divercity.app.data.networking.services.JobService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class JobRepositoryImpl @Inject
constructor(private val jobService: JobService) : JobRepository {

    override fun fetchSimilarJobs(pageNumber: Int, size: Int, jobId: String?): Observable<List<JobResponse>> {
        return jobService.fetchSimilarJobs(pageNumber, size, jobId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun performActionJob(jobId: String, action: String): Observable<JobResponse> {
        val partAction = RequestBody.create(MediaType.parse("text/plain"), action)

        return jobService.performActionJob(jobId, partAction).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchApplicants(jobId: String, pageNumber: Int, size: Int): Observable<List<JobApplicationResponse>> {
        return jobService.fetchApplicants(jobId, pageNumber, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun applyJob(jobId: String, userDocId: String, coverLetter: String): Observable<JobApplicationResponse> {
        val partJobId = RequestBody.create(MediaType.parse("text/plain"), jobId)
        val partDocId = RequestBody.create(MediaType.parse("text/plain"), userDocId)
        val partCL = RequestBody.create(MediaType.parse("text/plain"), coverLetter)

        return jobService.applyJob(partJobId, partDocId, partCL).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchJobById(jobId: String): Observable<JobResponse> {
        return jobService.fetchJobById(jobId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchJobPostingsByUser(userId: String, page: Int, size: Int, query: String?): Observable<List<JobResponse>> {
        return jobService.fetchJobPostingsByUser(userId, page, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchMyJobApplications(page: Int, size: Int, query: String?): Observable<List<JobApplicationResponse>> {
        return jobService.fetchMyJobApplications(page, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun shareJob(jobId: String, body: JobShareGroupBody): Observable<JobResponse> {
        return jobService.shareJob(jobId, body).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun postJob(body: JobBody): Observable<JobResponse> {
        return jobService.postJob(body).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchSavedJobs(page: Int, size: Int, query: String?): Observable<IncludedArray<JobResponse>> {
        return jobService.fetchSavedJobs(page, size, query)
    }

    override fun saveJob(jobId: String): Observable<JobResponse> {
        return jobService.saveJob(jobId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun removeSavedJob(jobId: String): Observable<JobResponse> {
        return jobService.removeSavedJob(jobId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchJobs(page: Int, size: Int, query: String?): Observable<List<JobResponse>> {
        return jobService.fetchJobs(page, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchJobTypes(): Observable<List<JobTypeResponse>> {
        return jobService.fetchJobTypes().map {
            checkResponse(it)
            it.body()?.data
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }

    override fun editJob(jobId: String, body: JobBody): Observable<JobResponse> {
        return jobService.editJob(jobId, body).map {
            checkResponse(it)
            it.body()?.data
        }
    }
}