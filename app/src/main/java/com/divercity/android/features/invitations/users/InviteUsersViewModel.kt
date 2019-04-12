package com.divercity.android.features.invitations.users

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.group.invitation.GroupInviteResponse
import com.divercity.android.data.entity.group.invitation.user.GroupInviteUser
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.newchat.datasource.UserPaginatedRepositoryImpl
import com.divercity.android.features.invitations.users.usecase.InviteUsersToGroupUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class InviteUsersViewModel @Inject
constructor(private val repository: UserPaginatedRepositoryImpl,
            private val inviteUsersToGroupUseCase: InviteUsersToGroupUseCase) : BaseViewModel() {

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedUserList: LiveData<PagedList<Any>>
    private lateinit var listingPaginatedJob: Listing<Any>
    private lateinit var lastSearch: String
    var inviteUsersResponse = SingleLiveEvent<Resource<String>>()

    init {
        fetchUsers(null, null)
    }

    val networkState: LiveData<NetworkState>
        get() = listingPaginatedJob.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchUsers(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, lastSearch)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, lastSearch)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String) {
        repository.clear()

        listingPaginatedJob = repository.fetchData(searchQuery)
        pagedUserList = listingPaginatedJob.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState.removeObservers(lifecycleOwner)
        refreshState.removeObservers(lifecycleOwner)
        pagedUserList.removeObservers(lifecycleOwner)
    }

    fun inviteToGroup(groupId: String, contacts: List<String>) {
        inviteUsersResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<GroupInviteResponse>() {
            override fun onFail(error: String) {
                inviteUsersResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                inviteUsersResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: GroupInviteResponse) {
                inviteUsersResponse.postValue(Resource.success("Group invitations sent successfully"))
            }
        }

        inviteUsersToGroupUseCase.execute(callback,
                InviteUsersToGroupUseCase.Params.forGroups(
                        GroupInviteUser(
                                groupId = groupId,
                                users = contacts
                        )
                ))
    }

    override fun onCleared() {
        super.onCleared()
        inviteUsersToGroupUseCase.dispose()
    }
}
