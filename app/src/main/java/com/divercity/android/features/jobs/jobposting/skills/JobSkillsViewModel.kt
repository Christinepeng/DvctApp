package com.divercity.android.features.jobs.jobposting.skills

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.jobs.jobposting.skills.datasource.SkillPaginatedRepositoryImpl
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
