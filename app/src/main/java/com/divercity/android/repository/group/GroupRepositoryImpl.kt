package com.divercity.android.repository.group

import androidx.paging.DataSource
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.company.companyadmin.body.Admin
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse
import com.divercity.android.data.entity.group.creategroup.CreateGroupBody
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.discardgroup.DiscardGroupsEntityBody
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.group.groupadmin.AddGroupAdminsBody
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContact
import com.divercity.android.data.entity.group.invitation.contact.GroupInviteContactBody
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUser
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUserBody
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.question.NewQuestionBody
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.networking.services.GroupService
import com.divercity.android.db.dao.GroupDao
import com.divercity.android.model.Question
import com.divercity.android.model.user.User
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 29/10/2018.
 */

class GroupRepositoryImpl @Inject
constructor(
    private val service: GroupService,
    private val groupDao: GroupDao
) : GroupRepository {

    override fun fetchRecommendedGroups(
        pageNumber: Int,
        size: Int
    ): Observable<List<GroupResponse>> {
        return service.fetchRecommendedGroups(pageNumber, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchAllGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<GroupResponse>> {
        return service.fetchAllGroups(page, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun requestToJoinGroup(groupId: String): Observable<MessageResponse> {
        return service.requestToJoinGroup(groupId)
    }

    override fun createGroup(group: GroupOfInterest): Observable<GroupResponse> {
        return service.createGroup(CreateGroupBody(group)).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun editGroup(groupId: String, group: GroupOfInterest): Observable<GroupResponse> {
        return service.editGroup(groupId, CreateGroupBody(group)).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchGroupAdmins(
        groupId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return service.fetchGroupAdmins(groupId, pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()?.data?.map { it.toUser() }
        }
    }

    override fun fetchQuestions(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<Question>> {
        return service.fetchQuestions(groupId, page, size, query).map { response ->
            checkResponse(response)
            response.body()?.data?.map { it.toQuestion() }
        }
    }

    override fun fetchGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>> {
        return service.fetchGroups(page, size, query).map {
            checkResponse(it)
            it.body()
        }
    }

    override fun fetchFollowedGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>> {
        return service.fetchFollowedGroups(page, size, query).map {
            checkResponse(it)
            it.body()
        }
    }

    override fun fetchTrendingGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>> {
        return service.fetchTrendingGroups(page, size, query).map {
            checkResponse(it)
            it.body()
        }
    }

    override fun fetchMyGroups(
        page: Int,
        size: Int,
        query: String?
    ): Observable<DataArray<GroupResponse>> {
        return service.fetchMyGroups(page, size, query).map {
            checkResponse(it)
            it.body()
        }
    }

    override fun fetchGroupMembers(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<User>> {
        return service.fetchGroupMembers(groupId, page, size, query).map { response ->
            checkResponse(response)
            response.body()?.data?.map { it.toUser() }
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }

    override fun inviteContact(invitations: GroupInviteContact): Observable<GroupInviteResponse> {
        return service.inviteContact(GroupInviteContactBody(invitations))
    }

    override fun inviteUser(invitations: GroupInviteUser): Observable<GroupInviteResponse> {
        return service.inviteUsers(GroupInviteUserBody(invitations))
    }

    override fun fetchGroupInvitations(
        page: Int,
        size: Int
    ): Observable<List<GroupInvitationNotificationResponse>> {
        return service.fetchGroupInvitations(page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchGroupJoinRequests(
        page: Int,
        size: Int
    ): Observable<List<JoinGroupRequestResponse>> {
        return service.fetchGroupJoinRequests(page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun createNewTopic(
        question: String,
        groupId: String,
        image: String?
    ): Observable<Question> {
        return service.createNewTopic(
            NewQuestionBody(question, groupId, image)
        ).map {
            checkResponse(it)
            it.body()?.data?.toQuestion()
        }
    }

    override fun fetchAnswers(
        questionId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<AnswerEntityResponse>> {
        return service.fetchAnswers(questionId, pageNumber, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun getPagedAnswersByQuestionId(questionId: Int): DataSource.Factory<Int, AnswerEntityResponse> {
        return groupDao.getPagedAnswersByQuestionId(questionId)
    }

    override suspend fun insertAnswers(answers: List<AnswerEntityResponse>) {
        return withContext(Dispatchers.IO) {
            groupDao.insertAnswers(answers)
        }
    }

    override suspend fun insertAnswer(answer: AnswerEntityResponse) {
        return withContext(Dispatchers.IO) {
            groupDao.insertAnswer(answer)
        }
    }

    override fun sendNewAnswer(body: AnswerBody): Observable<AnswerEntityResponse> {
        return service.sendNewAnswer(body).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchGroupById(groupId: String): Observable<GroupResponse> {
        return service.fetchGroupById(groupId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun deleteGroup(groupId: String): Observable<Boolean> {
        return service.deleteGroup(groupId).map {
            checkResponse(it)
            true
        }
    }

    override fun acceptGroupInvite(inviteId: String): Observable<Unit> {
        val partInviteId = RequestBody.create(MediaType.parse("text/plain"), inviteId)
        return service.acceptGroupInvite(partInviteId).map {
            checkResponse(it)
        }
    }

    override fun declineGroupInvite(inviteId: String): Observable<Unit> {
        val partInviteId = RequestBody.create(MediaType.parse("text/plain"), inviteId)
        return service.declineGroupInvite(partInviteId).map {
            checkResponse(it)
        }
    }

    override fun acceptJoinGroupRequest(
        groupId: String,
        userId: String
    ): Observable<Unit> {
        return service.acceptJoinGroupRequest(groupId, userId).map {
            checkResponse(it)
        }
    }

    override fun declineJoinGroupRequest(
        groupId: String,
        userId: String
    ): Observable<Unit> {
        return service.declineJoinGroupRequest(groupId, userId).map {
            checkResponse(it)
        }
    }

    override fun addGroupAdmins(
        body: AddGroupAdminsBody
    ): Observable<String> {
        return service.addGroupAdmins(body).map {
            checkResponse(it)
            it.body()?.message
        }
    }

    override fun acceptGroupAdminInvite(inviteId: String): Observable<Unit> {
        val partInviteId = RequestBody.create(MediaType.parse("text/plain"), inviteId)
        return service.acceptGroupAdminInvite(partInviteId).map {
            checkResponse(it)
        }
    }

    override fun declineGroupAdminInvite(inviteId: String): Observable<Unit> {
        val partInviteId = RequestBody.create(MediaType.parse("text/plain"), inviteId)
        return service.declineGroupAdminInvite(partInviteId).map {
            checkResponse(it)
        }
    }

    override fun deleteGroupAdmins(groupId: String, adminsId: List<String>): Observable<Unit> {
        return service.deleteGroupAdmins(groupId, Admin(adminsId)).map {
            checkResponse(it)
        }
    }

    override fun fetchFeedQuestions(
        pageNumber: Int,
        size: Int
    ): Observable<List<Question>> {
        return service.fetchFeedQuestions(pageNumber, size).map { response ->
            checkResponse(response)
            response.body()?.data?.map { it.toQuestion() }
        }
    }

    override fun fetchQuestionById(questionId: String): Observable<Question> {
        return service.fetchQuestionById(questionId).map { response ->
            checkResponse(response)
            response.body()?.data?.toQuestion()
        }
    }

    override fun discardRecommendedGroups(groupsId: List<String>): Observable<Unit> {
        return service.discardRecommendedGroups(DiscardGroupsEntityBody(groupsId))
            .map { response ->
                checkResponse(response)
            }
    }
}