package com.divercity.android.repository.group

import androidx.paging.DataSource
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.group.groupadmin.AddGroupAdminsBody
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContact
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUser
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.model.Question
import com.divercity.android.model.user.User
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

    fun fetchAllGroups(page: Int, size: Int, query: String?): Observable<List<GroupResponse>>

    fun fetchMyGroups(page: Int, size: Int, query: String?): Observable<DataArray<GroupResponse>>

    fun fetchGroupMembers(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun inviteContact(
        invitations: GroupInviteContact
    ): Observable<GroupInviteResponse>

    fun inviteUser(
        invitations: GroupInviteUser
    ): Observable<GroupInviteResponse>

    fun fetchQuestions(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<Question>>

    fun fetchGroupAdmins(
        groupId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>>

    fun createGroup(group: GroupOfInterest): Observable<GroupResponse>

    fun editGroup(
        groupId: String,
        group: GroupOfInterest
    ): Observable<GroupResponse>

    fun requestToJoinGroup(groupId: String): Observable<MessageResponse>

    fun fetchRecommendedGroups(
        pageNumber: Int,
        size: Int
    ): Observable<List<GroupResponse>>

    fun fetchGroupInvitations(
        page: Int,
        size: Int
    ): Observable<List<GroupInvitationNotificationResponse>>

    fun fetchGroupJoinRequests(
        page: Int,
        size: Int
    ): Observable<List<JoinGroupRequestResponse>>

    fun createNewTopic(
        question: String, groupId: String, image: String?
    ): Observable<Question>

    fun fetchAnswers(
        questionId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<AnswerEntityResponse>>

    fun deleteGroup(groupId: String): Observable<Boolean>

    fun fetchGroupById(groupId: String): Observable<GroupResponse>

    fun getPagedAnswersByQuestionId(questionId: Int): DataSource.Factory<Int, AnswerEntityResponse>

    suspend fun insertAnswers(answers: List<AnswerEntityResponse>)

    suspend fun insertAnswer(answer: AnswerEntityResponse)

    fun sendNewAnswer(body: AnswerBody): Observable<AnswerEntityResponse>

    fun acceptGroupInvite(inviteId: String): Observable<Unit>

    fun declineGroupInvite(inviteId: String): Observable<Unit>

    fun acceptJoinGroupRequest(
        groupId: String,
        userId: String
    ): Observable<Unit>

    fun declineJoinGroupRequest(
        groupId: String,
        userId: String
    ): Observable<Unit>

    fun addGroupAdmins(
        body: AddGroupAdminsBody
    ): Observable<String>

    fun acceptGroupAdminInvite(inviteId: String): Observable<Unit>

    fun declineGroupAdminInvite(inviteId: String): Observable<Unit>

    fun deleteGroupAdmins(
        groupId: String,
        adminsId: List<String>
    ): Observable<Unit>

    fun fetchFeedQuestions(pageNumber: Int, size: Int): Observable<List<Question>>

    fun fetchQuestionById(
        questionId: String
    ): Observable<Question>
}