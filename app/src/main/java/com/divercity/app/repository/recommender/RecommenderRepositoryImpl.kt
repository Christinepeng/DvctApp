package com.divercity.app.repository.recommender

import com.divercity.app.data.entity.recommendedgroups.RecommendedGroupsResponse
import com.divercity.app.data.networking.services.RecommendersService
import io.reactivex.Observable
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class RecommenderRepositoryImpl @Inject
constructor(private val recommendersService: RecommendersService) : RecommenderRepository {

    override fun fetchRecommendedGroups(
        pageNumber: Int,
        size: Int
    ): Observable<List<RecommendedGroupsResponse>> {
        return recommendersService.fetchRecommendedGroups(pageNumber, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }
}