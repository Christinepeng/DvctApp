package com.divercity.android.features.company.companyaddadmin

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.chat.newchat.datasource.UserPaginatedRepositoryImpl
import com.divercity.android.features.company.companyaddadmin.usecase.AddCompanyAdminUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAddAdminViewModel @Inject
constructor(
    private val repository: UserPaginatedRepositoryImpl,
    private val addCompanyAdminUseCase: AddCompanyAdminUseCase
) : BaseViewModel() {

    var addCompanyAdminResponse = SingleLiveEvent<Resource<List<CompanyAdminResponse>>>()

    var subscribeToPaginatedLiveData = SingleLiveEvent<Any>()
    lateinit var pagedUserList: LiveData<PagedList<Any>>
    private lateinit var listingPaginatedJob: Listing<Any>
    private lateinit var lastSearch: String

    init {
        fetchUsers(null, null)
    }

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


    val networkState: LiveData<NetworkState>
        get() = listingPaginatedJob.networkState

    val refreshState: LiveData<NetworkState>
        get() = listingPaginatedJob.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    fun addCompanyAdmins(companyId: String, admins: List<String>) {
        addCompanyAdminResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<CompanyAdminResponse>>() {
            override fun onFail(error: String) {
                addCompanyAdminResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addCompanyAdminResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<CompanyAdminResponse>) {
                addCompanyAdminResponse.postValue(Resource.success(o))
            }
        }
        addCompanyAdminUseCase.execute(
            callback,
            AddCompanyAdminUseCase.Params.forAdmins(companyId, admins)
        )
    }
}
