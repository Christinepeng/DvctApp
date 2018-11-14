package com.divercity.app.features.jobposting.skills

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.ui.NetworkState
import com.divercity.app.core.utils.Listing
import com.divercity.app.data.entity.skills.SkillResponse
import com.divercity.app.features.jobposting.skills.job.SkillPaginatedRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobSkillsViewModel @Inject
constructor(private val repository: SkillPaginatedRepositoryImpl) : BaseViewModel() {

    lateinit var pagedSkillsList: LiveData<PagedList<SkillResponse>>
    private lateinit var listingPaginatedSkill: Listing<SkillResponse>

    init {
        fetchSkills(null)
    }

    fun networkState(): LiveData<NetworkState> = listingPaginatedSkill.networkState

    fun refreshState(): LiveData<NetworkState> = listingPaginatedSkill.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun fetchSkills(searchQuery : String?) {
        listingPaginatedSkill = repository.fetchData(searchQuery)
        pagedSkillsList = listingPaginatedSkill.pagedList
    }
}
