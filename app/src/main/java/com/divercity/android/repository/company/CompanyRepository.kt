package com.divercity.android.repository.company

import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable

/**
 * Created by lucas on 23/11/2018.
 */

interface CompanyRepository {

    fun fetchCompaniesIamAdmin(
        page: Int,
        size: Int
    ): Observable<List<CompanyResponse>>

    fun fetchEmployeesByCompany(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<UserResponse>>

    fun fetchCompanyAdmins(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyAdminResponse>>

    fun addCompanyAdmin(
        companyId: String,
        admins : AddAdminCompanyBody
    ): Observable<List<CompanyAdminResponse>>

    fun deleteCompanyAdmins(
        companyId: String,
        adminsId: List<String>
    ): Observable<Unit>
}