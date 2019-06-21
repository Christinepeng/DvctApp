package com.divercity.android.repository.data

import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.occupationofinterests.OOIResponse
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import com.divercity.android.data.entity.recommendedjobsgoi.RecommendedJobsGOIResponse
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.model.Degree
import com.divercity.android.model.Major
import com.divercity.android.model.School
import io.reactivex.Observable

/**
 * Created by lucas on 18/10/2018.
 */

interface DataRepository {

    fun fetchCompanies(page: Int, size: Int, query: String?): Observable<List<CompanyResponse>>

    fun fetchIndustries(page: Int, size: Int, query: String?): Observable<List<IndustryResponse>>

    fun fetchSchool(page: Int, size: Int, query: String?): Observable<List<School>>

    fun fetchMajors(page: Int, size: Int, query: String?): Observable<List<Major>>

    fun fetchLocations(page: Int, size: Int, query: String?): Observable<List<LocationResponse>>

    fun fetchSkills(page: Int, size: Int, query: String?): Observable<List<SkillResponse>>

    fun createCompany(body: CreateCompanyBody): Observable<Boolean>

    fun fetchCompanySizes(): Observable<List<CompanySizeResponse>>

    fun fetchInterests(): Observable<List<InterestsResponse>>

    fun searchPhotos(query: String): Observable<List<PhotoEntityResponse>>

    fun fetchOccupationOfInterests(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<OOIResponse>>

    fun fetchDegrees(): Observable<List<Degree>>

    fun fetchRecommendedJobsGOIS(
        pageNumber: Int,
        size: Int
    ): Observable<RecommendedJobsGOIResponse>
}
