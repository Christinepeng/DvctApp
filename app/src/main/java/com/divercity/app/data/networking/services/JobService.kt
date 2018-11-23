package com.divercity.app.data.networking.services

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.base.IncludedArray
import com.divercity.app.data.entity.job.jobpostingbody.JobBody
import com.divercity.app.data.entity.job.jobsharedgroupbody.JobShareGroupBody
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import com.divercity.app.data.entity.job.response.JobResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface JobService {

    @GET("jobs")
    fun fetchJobs(@Query("page[number]") pageNumber: Int,
                  @Query("page[size]") size: Int,
                  @Query("search_query") query: String?): Observable<DataArray<JobResponse>>

    @POST("jobs/{jobId}/bookmark")
    fun saveJob(@Path("jobId") jobId: String): Observable<DataObject<JobResponse>>

    @DELETE("jobs/{jobId}/remove_bookmark")
    fun removeSavedJob(@Path("jobId") jobId: String): Observable<DataObject<JobResponse>>

    @GET("bookmarks?type=Job")
    fun fetchSavedJobs(@Query("page[number]") pageNumber: Int,
                       @Query("page[size]") size: Int,
                       @Query("search_query") query: String?): Observable<IncludedArray<JobResponse>>

    @GET("jobs/applications")
    fun fetchMyJobApplications(@Query("page[number]") pageNumber: Int,
                       @Query("page[size]") size: Int,
                       @Query("search_query") query: String?): Observable<IncludedArray<JobResponse>>

    @GET("jobs/job_types")
    fun fetchJobTypes(): Observable<DataArray<JobTypeResponse>>

    @GET("jobs")
    fun fetchJobPostingsByUser(@Query("user") userId: String,
                               @Query("page[number]") pageNumber: Int,
                               @Query("page[size]") size: Int,
                               @Query("search_query") query: String?): Observable<DataArray<JobResponse>>

    @POST("jobs")
    fun postJob(@Body() body: JobBody): Observable<DataObject<JobResponse>>

    @POST("jobs/{jobId}/share")
    fun shareJob(@Path("jobId") jobId: String, @Body() body: JobShareGroupBody): Observable<DataObject<JobResponse>>
}