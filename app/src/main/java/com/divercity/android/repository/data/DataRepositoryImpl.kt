package com.divercity.android.repository.data

import com.divercity.android.core.base.BaseRepository
import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.location.LocationResponse
import com.divercity.android.data.entity.occupationofinterests.OOIResponse
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.data.networking.services.DataService
import com.divercity.android.model.Major
import com.divercity.android.model.School
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by lucas on 18/10/2018.
 */

class DataRepositoryImpl @Inject
constructor(private val service: DataService) : BaseRepository(), DataRepository {

    override fun searchPhotos(query: String): Observable<List<PhotoEntityResponse>> {
        return service.searchPhotos(query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchCompanies(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<CompanyResponse>> {
        return service.fetchCompanies(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchIndustries(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<IndustryResponse>> {
        return service.fetchIndustries(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchSchool(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<School>> {
        return service.fetchSchools(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map {
                it.toSchool()
            }
        }
    }

    override fun fetchMajors(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<Major>> {
        return service.fetchStudentMajors(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data.map {
                it.toMajor()
            }
        }
    }

    override fun fetchLocations(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<LocationResponse>> {
        return service.fetchLocations(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchSkills(
        page: Int,
        size: Int,
        query: String?
    ): Observable<List<SkillResponse>> {
        return service.fetchSkills(page, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun createCompany(body: CreateCompanyBody): Observable<Boolean> {
        return service.createCompany(body).map { response ->
            checkResponse(response)
            true
        }
    }

    override fun fetchCompanySizes(): Observable<List<CompanySizeResponse>> {
        return service.fetchCompanySizes().map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchInterests(): Observable<List<InterestsResponse>> {
        return service.fetchInterests().map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }

    override fun fetchOccupationOfInterests(
        pageNumber: Int,
        size: Int,
        query: String?
    ): Observable<List<OOIResponse>> {
        return service.fetchOccupationOfInterests(pageNumber, size, query).map { response ->
            checkResponse(response)
            response.body()!!.data
        }
    }
}
