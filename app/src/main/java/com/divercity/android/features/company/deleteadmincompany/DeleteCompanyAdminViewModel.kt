package com.divercity.android.features.company.deleteadmincompany

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.company.companyadmin.CompanyAdminPaginatedRepository
import com.divercity.android.features.company.deleteadmincompany.usecase.DeleteCompanyAdminsUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteCompanyAdminViewModel @Inject
constructor(
    repository: CompanyAdminPaginatedRepository,
    private val deleteCompanyAdminsUseCase: DeleteCompanyAdminsUseCase
) : BaseViewModelPagination<CompanyAdminResponse>(repository) {

    var deleteCompanyAdminResponse = SingleLiveEvent<Resource<Unit>>()

    var companyId: String = ""
        set(value) {
            field = value
            (repository as CompanyAdminPaginatedRepository).setCompanyId(value)
        }

    fun deleteCompanyAdmins(adminsId: List<String>) {
        deleteCompanyAdminResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<Unit>() {
            override fun onFail(error: String) {
                deleteCompanyAdminResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                deleteCompanyAdminResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: Unit) {
                deleteCompanyAdminResponse.postValue(Resource.success(o))
            }
        }
        deleteCompanyAdminsUseCase.execute(
            callback,
            DeleteCompanyAdminsUseCase.Params(companyId, adminsId)
        )
    }
}
