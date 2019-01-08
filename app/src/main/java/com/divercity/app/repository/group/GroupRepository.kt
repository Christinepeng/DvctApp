package com.divercity.app.repository.group

import com.divercity.app.data.entity.base.DataArray
import com.divercity.app.data.entity.group.GroupResponse
import com.divercity.app.data.entity.questions.QuestionResponse
import com.divercity.app.data.entity.user.response.UserResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface GroupRepository {

    fun fetchGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchFollowedGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchTrendingGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchMyGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchGroupMembers(groupId: String, page: Int, size: Int, query: String?): Observable<List<UserResponse>>

    fun createGroup(title: String,
                             description: String,
                             groupType: String,
                             picture: String): Observable<GroupResponse>

    fun fetchQuestions(groupId: String, page: Int, size: Int, query: String?): Observable<List<QuestionResponse>>

    fun fetchGroupAdmins(groupId: String,
                         pageNumber: Int,
                         size: Int,
                         query: String?): Observable<List<UserResponse>>
}