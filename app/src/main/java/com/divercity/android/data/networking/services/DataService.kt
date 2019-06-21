package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.entity.degree.DegreeEntityResponse
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.major.MajorEntityResponse
import com.divercity.android.data.entity.occupationofinterests.OOIResponse
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import com.divercity.android.data.entity.recommendedjobsgoi.RecommendedJobsGOIResponse
import com.divercity.android.data.entity.school.SchoolEntityResponse
import com.divercity.android.data.entity.skills.SkillResponse

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by lucas on 09/09/2018.
 */

interface DataService {

    @GET("jobs/job_employers")
    fun fetchCompanies(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<CompanyResponse>>>

    @GET("recommenders/group_of_interests_jobs")
    fun fetchRecommendedJobsGOIs(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int
    ): Observable<RecommendedJobsGOIResponse>

    @GET("data/industries")
    fun fetchIndustries(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<IndustryResponse>>>

    @GET("schools")
    fun fetchSchools(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<SchoolEntityResponse>>>

    @GET("data/student_majors")
    fun fetchStudentMajors(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<MajorEntityResponse>>>

    @GET("data/company_sizes")
    fun fetchCompanySizes(): Observable<Response<DataArray<CompanySizeResponse>>>

    @GET("data/cities")
    fun fetchLocations(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<LocationResponse>>>

    @GET("data/job_skills")
    fun fetchSkills(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<SkillResponse>>>

    @GET("data/occupation_of_interests")
    fun fetchOccupationOfInterests(
        @Query("page[number]") pageNumber: Int,
        @Query("page[size]") size: Int,
        @Query("search_query") query: String?
    ): Observable<Response<DataArray<OOIResponse>>>

    @GET("interests")
    fun fetchInterests(): Observable<Response<DataArray<InterestsResponse>>>

    @GET("group_of_interests/suggest_group_photo")
    fun searchPhotos(
        @Query("query") query: String
    ): Observable<Response<DataArray<PhotoEntityResponse>>>

    @POST("job_employers")
    fun createCompany(@Body body: CreateCompanyBody): Observable<Response<Void>>

    @GET("data/degrees")
    fun fetchDegrees(): Observable<Response<DataArray<DegreeEntityResponse>>>
}
