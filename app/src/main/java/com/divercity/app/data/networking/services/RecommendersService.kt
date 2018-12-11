package com.divercity.app.data.networking.services

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.recommendedgroups.RecommendedGroupsResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lucas on 23/11/2018.
 */

interface RecommendersService {

    @GET("recommenders/group_of_interests")
    fun fetchRecommendedGroups(@Query("page[number]") pageNumber: Int,
                               @Query("page[size]") size: Int)
            : Observable<Response<DataArray<RecommendedGroupsResponse>>>

    @GET("recommenders/jobs")
    fun fetchRecommendedJobs(): Observable<Response<DataArray<RecommendedGroupsResponse>>>
}