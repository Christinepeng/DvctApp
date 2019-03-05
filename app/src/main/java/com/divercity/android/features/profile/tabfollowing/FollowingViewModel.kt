package com.divercity.android.features.profile.tabfollowing

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.profile.tabfollowing.datasource.FollowingPaginatedRepositoryImpl
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