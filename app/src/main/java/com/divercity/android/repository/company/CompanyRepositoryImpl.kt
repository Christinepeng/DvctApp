package com.divercity.android.repository.company

import com.divercity.android.core.base.BaseRepository
import com.divercity.android.data.entity.company.companyadmin.body.AddAdminCompanyBody
import com.divercity.android.data.entity.company.companyadmin.deleteadminbody.Admin
import com.divercity.android.data.entity.company.companyadmin.deleteadminbody.DeleteCompanyAdminBody
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminEntityResponse
import com.divercity.android.data.entity.company.rating.Rating
import com.divercity.android.data.entity.company.rating.RatingBody
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.data.networking.services.CompanyService
import com.divercity.android.model.user.User
import io.reactivex.Observable
import javax.inject.Inject


/**
 * Created by lucas on 23/11/2018.
 */

class CompanyRepositoryImpl @Inject
constructor(
    private val service: CompanyService
) : BaseRepository(), CompanyRepository {

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
    ): Observable<List<User>> {
        return service.fetchEmployeesByCompany(companyId, page, size).map { response ->
            checkResponse(response)
            response.body()!!.data.map { it.toUser() }
        }
    }

    override fun fetchCompanyAdmins(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyAdminEntityResponse>> {
        return service.fetchCompanyAdmins(companyId, page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun addCompanyAdmin(
        companyId: String,
        admins: AddAdminCompanyBody
    ): Observable<List<CompanyAdminEntityResponse>> {
        return service.addCompanyAdmin(companyId, admins).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun deleteCompanyAdmins(
        companyId: String,
        adminsId: List<String>
    ): Observable<Unit> {
        return service.deleteCompanyAdmin(companyId, DeleteCompanyAdminBody(Admin(adminsId))).map {
            checkResponse(it)
        }
    }

    override fun fetchCompany(companyId: String): Observable<CompanyResponse> {
        return service.fetchCompany(companyId).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun fetchCompanyDiversityReviews(
        companyId: String,
        page: Int,
        size: Int
    ): Observable<List<CompanyDiversityReviewResponse>> {
        return service.fetchCompanyDiversityReviews(companyId, page, size).map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun rateCompany(
        companyId: String,
        rating: Rating
    ): Observable<CompanyDiversityReviewResponse> {
        return service.rateCompany(companyId, RatingBody(rating)).map {
            checkResponse(it)
            it.body()?.data
        }
    }
}