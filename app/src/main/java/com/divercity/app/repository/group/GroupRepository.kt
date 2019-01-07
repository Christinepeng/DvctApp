package com.divercity.app.repository.group

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.group.GroupResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface GroupRepository {

    fun fetchGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchFollowedGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchTrendingGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchMyGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun createGroup(title: String,
                             description: String,
                             groupType: String,
                             picture: String): Observable<GroupResponse>
}