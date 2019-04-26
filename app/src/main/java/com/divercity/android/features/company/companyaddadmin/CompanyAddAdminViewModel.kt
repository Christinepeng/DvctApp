package com.divercity.android.features.company.companyaddadmin

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.companyaddadmin.usecase.AddCompanyAdminUseCase
import com.divercity.android.repository.paginated.UsersByCharacterPaginatedRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class CompanyAddAdminViewModel @Inject
constructor(
    repository: UsersByCharacterPaginatedRepository,
    private val addCompanyAdminUseCase: AddCompanyAdminUseCase
) : BaseViewModelPagination<Any>(repository) {

    var addCompanyAdminResponse = SingleLiveEvent<Resource<List<CompanyAdminEntityResponse>>>()

    init {
        fetchData(null, "")
    }

    fun addCompanyAdmins(companyId: String, admins: List<String>) {
        addCompanyAdminResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<CompanyAdminEntityResponse>>() {
            override fun onFail(error: String) {
                addCompanyAdminResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                addCompanyAdminResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<CompanyAdminEntityResponse>) {
                addCompanyAdminResponse.postValue(Resource.success(o))
            }
        }
        addCompanyAdminUseCase.execute(
            callback,
            AddCompanyAdminUseCase.Params.forAdmins(companyId, admins)
        )
    }
}
