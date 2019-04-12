package com.divercity.android.features.company.companydetail.employees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.core.utils.Listing
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.companydetail.employees.datasource.EmployeesPaginatedRepositoryImpl
import com.divercity.android.features.profile.usecase.ConnectUserUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class EmployeesViewModel @Inject
constructor(
    private val repository: EmployeesPaginatedRepositoryImpl,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModel() {

    lateinit var pagedListEmployees: LiveData<PagedList<UserResponse>>
    private var listingEmployees: Listing<UserResponse>? = null
    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()

    fun fetchEmployees(userId: String) {
       /* To call it once, because as it has parameters, and I am not starting viewmodels
        with params*/
        if (listingEmployees == null) {
            listingEmployees = repository.fetchData(userId)
            pagedListEmployees = listingEmployees!!.pagedList
        }
    }

    fun networkState(): LiveData<NetworkState> = listingEmployees!!.networkState

    fun refreshState(): LiveData<NetworkState> = listingEmployees!!.refreshState

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()

    override fun onCleared() {
        super.onCleared()
        repository.clear()
    }

    fun connectToUser(userId: String) {
        connectUserResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<ConnectUserResponse>() {
            override fun onFail(error: String) {
                connectUserResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                connectUserResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: ConnectUserResponse) {
                connectUserResponse.postValue(Resource.success(o))
            }
        }
        connectUserUseCase.execute(
            callback,
            ConnectUserUseCase.Params.toFollow(userId)
        )
    }
}