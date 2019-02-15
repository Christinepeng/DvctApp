package com.divercity.android.repository.group

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.group.contactinvitation.body.GroupInvite
import com.divercity.android.data.entity.group.contactinvitation.body.GroupInviteBody
import com.divercity.android.data.entity.group.contactinvitation.response.GroupInviteResponse
import com.divercity.android.data.entity.group.creategroup.CreateGroupBody
import com.divercity.android.data.entity.group.creategroup.GroupOfInterest
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.message.MessageResponse
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.GroupService
import io.reactivex.Observable
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
    private val service: GroupService
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

    override fun createGroup(
        title: String,
        description: String,
        groupType: String,
        picture: String
    ): Observable<GroupResponse> {
        val partTitle = RequestBody.create(MediaType.parse("text/plain"), title)
        val partDescription = RequestBody.create(MediaType.parse("text/plain"), description)
        val partGroupType = RequestBody.create(MediaType.parse("text/plain"), groupType)
        val partPicture = RequestBody.create(MediaType.parse("text/plain"), picture)

        return service.createGroup(partTitle, partDescription, partGroupType, partPicture).map {
            checkResponse(it)
            it.body()?.data
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
    ): Observable<List<GroupInviteResponse>> {
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
}