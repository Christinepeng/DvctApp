package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.base.IncludedArray
import com.divercity.android.data.entity.job.jobpostingbody.JobBody
import com.divercity.android.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.android.data.entity.job.jobtype.JobTypeResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.data.entity.jobapplication.JobApplicationResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface JobService {

    @GET("jobs")
    fun fetchJobs(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<JobResponse>>>

    @GET("jobs")
    fun fetchSimilarJobs(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("similar_to") jobId: String?
    ): Observable<Response<DataArray<JobResponse>>>

    @POST("jobs/{jobId}/bookmark")
    fun saveJob(@Path("jobId") jobId: String): Observable<Response<DataObject<JobResponse>>>

    @DELETE("jobs/{jobId}/remove_bookmark")
    fun removeSavedJob(@Path("jobId") jobId: String): Observable<Response<DataObject<JobResponse>>>

    @GET("bookmarks?type=Job")
    fun fetchSavedJobs(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<IncludedArray<JobResponse>>

    @GET("jobs/applications")
    fun fetchMyJobApplications(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<JobApplicationResponse>>>

    @GET("jobs/job_types")
    fun fetchJobTypes(): Observable<Response<DataArray<JobTypeResponse>>>

    @GET("jobs/{jobId}/application")
    fun fetchJobApplication(@Path("jobId") applicationId: String)
            : Observable<Response<DataObject<JobApplicationResponse>>>

    @GET("jobs/{id}")
    fun fetchJobById(@Path("id") jobId: String): Observable<Response<DataObject<JobResponse>>>

    @GET("jobs")
    fun fetchJobPostingsByUser(
        @Query("user") userId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<JobResponse>>>

    @POST("jobs")
    fun postJob(@Body() body: JobBody): Observable<Response<DataObject<JobResponse>>>

    @POST("jobs/{jobId}/share")
    fun shareJob(@Path("jobId") jobId: String, @Body() body: JobShareGroupBody): Observable<Response<DataObject<JobResponse>>>

    @Multipart
    @POST("jobs/apply")
    fun applyJob(
        @Part("application[job_id]") jobId: RequestBody,
        @Part("application[user_document_id]") userDocId: RequestBody,
        @Part("application[cover_letter]") coverLetter: RequestBody
    ): Observable<Response<DataObject<JobApplicationResponse>>>

    @GET("jobs/{jobId}/applicants")
    fun fetchApplicants(
        @Path("jobId") jobId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<JobApplicationResponse>>>

    //    Actions: publis, unpublish, delete
    @Multipart
    @POST("jobs/{jobId}/perform")
    fun performActionJob(
        @Path("jobId") jobId: String,
        @Part("intent") action: RequestBody
    ): Observable<Response<DataObject<JobResponse>>>

    @PUT("jobs/{jobId}")
    fun editJob(@Path("jobId") jobId: String, @Body() body: JobBody): Observable<Response<DataObject<JobResponse>>>

    @Multipart
    @PUT("jobs/{jobId}/apply_edit")
    fun editJobApplication(
        @Path("jobId") jobId: String,
        @Part("application[user_document_id]") userDocId: RequestBody,
        @Part("application[cover_letter]") coverLetter: RequestBody
    ): Observable<Response<DataObject<JobApplicationResponse>>>

    @Multipart
    @PUT("jobs/{jobId}/apply_edit")
    fun cancelJobApplication(
        @Path("jobId") jobId: String,
        @Part("application[canceled]") canceled: RequestBody
    ): Observable<Response<DataObject<JobApplicationResponse>>>

    @GET("recommenders/jobs")
    fun fetchRecommendedJobs(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<JobResponse>>>
}