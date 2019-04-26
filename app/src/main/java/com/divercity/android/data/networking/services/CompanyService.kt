package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.deleteadminbody.DeleteCompanyAdminBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import com.divercity.android.data.entity.company.rating.RatingBody
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.data.entity.user.response.UserEntityResponse
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
    ): Observable<Response<DataArray<UserEntityResponse>>>

    @GET("job_employers/{companyId}/view_admins")
    fun fetchCompanyAdmins(
        @Path("companyId") companyId: String,
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<CompanyAdminEntityResponse>>>

    @POST("job_employers/{companyId}/create_admin")
    fun addCompanyAdmin(
        @Path("companyId") companyId: String,
        @Body admins: AddAdminCompanyBody
    ): Observable<Response<DataArray<CompanyAdminEntityResponse>>>

    @HTTP(method = "DELETE", path = "job_employers/{companyId}/remove_admin", hasBody = true)
    fun deleteCompanyAdmin(
        @Path("companyId") companyId: String,
        @Body deleteCompanyAdminBody: DeleteCompanyAdminBody
    ): Observable<Response<Unit>>

    @GET("job_employers/{companyId}")
    fun fetchCompany(
        @Path("companyId") companyId: String
    ): Observable<Response<DataObject<CompanyResponse>>>

    @GET("job_employers/{companyId}/reviews")
    fun fetchCompanyDiversityReviews(
        @Path("companyId") companyId: String,
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int
    ): Observable<Response<DataArray<CompanyDiversityReviewResponse>>>

    @POST("job_employers/{companyId}/rate")
    fun rateCompany(
        @Path("companyId") companyId: String,
        @Body ratingBody: RatingBody
    ): Observable<Response<DataObject<CompanyDiversityReviewResponse>>>
}