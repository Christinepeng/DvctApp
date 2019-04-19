package com.divercity.android.repository.company

import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.deleteadminbody.DeleteCompanyAdminBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.CompanyService
import io.reactivex.Observable
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject


/**
 * Created by lucas on 23/11/2018.
 */

class CompanyRepositoryImpl @Inject
constructor(
    private val service: CompanyService
) : CompanyRepository {

    override fun fetchCompaniesIamAdmin(
        page: Int,
        size: Int
    ): Observable<List<CompanyResponse>> {
        return service.fetchCompaniesIamAdmin(page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchEmployeesByCompany(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<UserResponse>> {
        return service.fetchEmployeesByCompany(companyId, page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchCompanyAdmins(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyAdminResponse>> {
        return service.fetchCompanyAdmins(companyId, page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun addCompanyAdmin(
        companyId: String,
        admins: AddAdminCompanyBody
    ): Observable<List<CompanyAdminResponse>> {
        return service.addCompanyAdmin(companyId, admins).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun deleteCompanyAdmins(
        companyId: String,
        adminsId: List<String>
    ): Observable<Unit> {
        return service.deleteCompanyAdmin(companyId, DeleteCompanyAdminBody(adminsId)).map {
            checkResponse(it)
        }
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }
}