package com.divercity.android.features.company.companydetail.employees

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.connectuser.response.ConnectUserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.user.usecase.ConnectUserUseCase
import com.divercity.android.model.user.User
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class EmployeesViewModel @Inject
constructor(
    repository: EmployeesPaginatedRepository,
    private val connectUserUseCase: ConnectUserUseCase
) : BaseViewModelPagination<User>(repository) {

    private var companyId: String? = null

    fun fetchEmployees(companyId: String) {
        if (this.companyId == null) {
            this.companyId = companyId
            (repository as EmployeesPaginatedRepository).setCompanyId(companyId)
            fetchData()
        }
    }

    var connectUserResponse = MutableLiveData<Resource<ConnectUserResponse>>()

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