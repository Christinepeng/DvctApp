package com.divercity.android.repository.company

import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import com.divercity.android.data.entity.company.rating.Rating
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.model.CompanyDiversityReview
import com.divercity.android.model.user.User
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
    ): Observable<List<User>>

    fun fetchCompanyAdmins(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyAdminEntityResponse>>

    fun addCompanyAdmin(
        companyId: String,
        admins: AddAdminCompanyBody
    ): Observable<List<CompanyAdminEntityResponse>>

    fun deleteCompanyAdmins(
        companyId: String,
        adminsId: List<String>
    ): Observable<Unit>

    fun fetchCompany(
        companyId: String
    ): Observable<CompanyResponse>

    fun fetchCompanyDiversityReviews(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyDiversityReview>>

    fun rateCompany(
        companyId: String,
        rating: Rating
    ): Observable<CompanyDiversityReviewEntityResponse>
}