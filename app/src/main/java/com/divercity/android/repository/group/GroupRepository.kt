package com.divercity.android.repository.group

import android.arch.paging.DataSource
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.group.contactinvitation.body.GroupInvite
import com.divercity.android.data.entity.group.contactinvitation.response.GroupInviteResponse
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface GroupRepository {

    fun fetchGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchFollowedGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>>

    fun fetchTrendingGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>>

    fun fetchAllGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchMyGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchGroupMembers(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun inviteContact(
        invitations: GroupInvite
    ): Observable<GroupInviteResponse>

    fun createGroup(
        title: String,
        description: String,
        groupType: String,
        picture: String
    ): Observable<GroupResponse>

    fun fetchQuestions(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<QuestionResponse>>

    fun fetchGroupAdmins(
        groupId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<UserResponse>>

    fun createGroup(group: GroupOfInterest): Observable<GroupResponse>

    fun requestToJoinGroup(groupId: String): Observable<MessageResponse>

    fun fetchRecommendedGroups(
        pageNumber: Int,
        size: Int
    ): Observable<List<GroupResponse>>

    fun fetchGroupInvitations(
        page: Int,
        size: Int
    ): Observable<List<GroupInviteResponse>>

    fun fetchGroupJoinRequests(
        page: Int,
        size: Int
    ): Observable<List<JoinGroupRequestResponse>>

    fun createNewTopic(
        question : String, groupId : String, image : String?
    ): Observable<QuestionResponse>

    fun fetchAnswers(
       questionId: String,
       pageNumber: Int,
       size: Int,
       query: String?
    ): Observable<List<AnswerResponse>>

    fun getPagedAnswersByQuestionId(questionId : Int): DataSource.Factory<Int, AnswerResponse>

    suspend fun insertAnswers(list : List<AnswerResponse>)

    suspend fun insertAnswer(answer : AnswerResponse)

    fun sendNewAnswer(body : AnswerBody): Observable<AnswerResponse>
}