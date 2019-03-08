package com.divercity.android.features.skill.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.jobs.jobposting.skills.datasource.SkillPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class SelectSkillViewModel @Inject
constructor(private val repository: SkillPaginatedRepositoryImpl
            ) : BaseViewModel() {

    lateinit var pagedSkillsList: LiveData<PagedList<SkillResponse>>
    private lateinit var listingPaginatedSkill: Listing<SkillResponse>
    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()
    private var lastSearch: String? = null

    init {
        fetchSkills(null, null)
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedSkill.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedSkill.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchSkills(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        if (searchQuery == null) {
            lastSearch = ""
            fetchData(lifecycleOwner, searchQuery)
        } else if (searchQuery != lastSearch) {
            lastSearch = searchQuery
            fetchData(lifecycleOwner, searchQuery)
        }
    }

    private fun fetchData(lifecycleOwner: LifecycleOwner?, searchQuery: String?) {
        listingPaginatedSkill = repository.fetchData(searchQuery)
        pagedSkillsList = listingPaginatedSkill.pagedList

        lifecycleOwner?.let {
            removeObservers(it)
            subscribeToPaginatedLiveData.call()
        }
    }

    private fun removeObservers(lifecycleOwner: LifecycleOwner) {
        networkState().removeObservers(lifecycleOwner)
        refreshState().removeObservers(lifecycleOwner)
        pagedSkillsList.removeObservers(lifecycleOwner)
    }
}
