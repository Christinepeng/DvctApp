package com.divercity.app.data.networking.services

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.job.JobResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lucas on 29/10/2018.
 */

interface JobService {

    @GET("jobs")
    fun fetchJobs(@Query("page[number]") pageNumber: Int,
                  @Query("page[size]") size: Int): Observable<DataArray<JobResponse>>
}