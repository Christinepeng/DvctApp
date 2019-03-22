package com.divercity.android.repository.group

import androidx.paging.DataSource
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.answer.body.AnswerBody
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.group.contactinvitation.body.GroupInvite
import com.divercity.android.data.entity.group.contactinvitation.body.GroupInviteBody
import com.divercity.android.data.entity.group.contactinvitation.response.GroupInviteResponse
import com.divercity.android.data.entity.group.creategroup.CreateGroupBody
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.question.NewQuestionBody
import com.divercity.android.data.entity.group.question.Question
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.GroupService
import com.divercity.android.db.dao.GroupDao
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    ): Observable<DataArray<GroupResponse>> {
        return service.fetchAllGroups(page, size, query).map {
            checkResponse(it)
            it.body()
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
    ): Observable<List<UserResponse>> {
        return service.fetchGroupAdmins(groupId, pageNumber, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchQuestions(
        groupId: String,
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<QuestionResponse>> {
        return service.fetchQuestions(groupId, page, size, query).map {
            checkResponse(it)
            it.body()?.data
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
    ): Observable<List<UserResponse>> {
        return service.fetchGroupMembers(groupId, page, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }

    override fun inviteContact(invitations: GroupInvite): Observable<GroupInviteResponse> {
        return service.inviteContact(GroupInviteBody(invitations))
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
    ): Observable<QuestionResponse> {
        return service.createNewTopic(
            NewQuestionBody(
                Question(
                    text = question,
                    groupOfInterestIds = listOf(groupId),
                    image = image
                )
            )
        ).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchAnswers(
        questionId: String,
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<AnswerResponse>> {
        return service.fetchAnswers(questionId, pageNumber, size, query).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun getPagedAnswersByQuestionId(questionId: Int): DataSource.Factory<Int, AnswerResponse> {
        return groupDao.getPagedAnswersByQuestionId(questionId)
    }

    override suspend fun insertAnswers(answers: List<AnswerResponse>) {
        return withContext(Dispatchers.IO) {
            groupDao.insertAnswers(answers)
        }
    }

    override suspend fun insertAnswer(answer: AnswerResponse) {
        return withContext(Dispatchers.IO) {
            groupDao.insertAnswer(answer)
        }
    }

    override fun sendNewAnswer(body: AnswerBody): Observable<AnswerResponse> {
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
}