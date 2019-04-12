package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.deleteadminbody.DeleteCompanyAdminBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.user.response.UserResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 29/10/2018.
 */

interface CompanyService {

    @GET("job_employers/mine")
    fun fetchCompaniesIamAdmin(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<CompanyResponse>>>

    @GET("job_employers/{companyId}/employees")
    fun fetchEmployeesByCompany(
        @Path("companyId") companyId: String,
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<UserResponse>>>

    @GET("job_employers/{companyId}/view_admins")
    fun fetchCompanyAdmins(
        @Path("companyId") companyId: String,
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<CompanyAdminResponse>>>

    @POST("job_employers/{companyId}/create_admin")
    fun addCompanyAdmin(
        @Path("companyId") companyId: String,
        @Body admins: AddAdminCompanyBody
    ): Observable<Response<DataArray<CompanyAdminResponse>>>

    @HTTP(method = "DELETE", path = "job_employers/{companyId}/remove_admin", hasBody = true)
    fun deleteCompanyAdmin(
        @Body deleteCompanyAdminBody: DeleteCompanyAdminBody
    ): Observable<Response<DataArray<CompanyAdminResponse>>>
}