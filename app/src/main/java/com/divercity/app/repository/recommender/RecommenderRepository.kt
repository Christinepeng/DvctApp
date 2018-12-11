package com.divercity.app.repository.recommender

import com.divercity.app.data.entity.recommendedgroups.RecommendedGroupsResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface RecommenderRepository {

    fun fetchRecommendedGroups(pageNumber: Int,
                               size: Int): Observable<List<RecommendedGroupsResponse>>
}