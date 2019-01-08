package com.divercity.app.features.profile.tabfollowing

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.user.response.UserResponse
import com.divercity.app.features.profile.tabfollowing.datasource.FollowingPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class FollowingViewModel @Inject
constructor(private val repository: FollowingPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedApplicantsList: LiveData<PagedList<UserResponse>>
    private lateinit var listingPaginatedJob: Listing<UserResponse>

    init {
        fetchFollowing()
    }

    private fun fetchFollowing() {
        listingPaginatedJob = repository.fetchData()
        pagedApplicantsList = listingPaginatedJob.pagedList
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedJob.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}