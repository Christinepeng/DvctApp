package com.divercity.android.data.networking.services;

import com.divercity.android.data.entity.base.DataArray;
import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse;
import com.divercity.android.data.entity.industry.IndustryResponse;
import com.divercity.android.data.entity.interests.InterestsResponse;
import com.divercity.android.data.entity.location.LocationResponse;
import com.divercity.android.data.entity.major.MajorResponse;
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.data.entity.skills.SkillResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lucas on 09/09/2018.
 */

public interface DataService {

    @GET("jobs/job_employers")
    Observable<Response<DataArray<CompanyResponse>>> fetchCompanies(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("data/industries")
    Observable<Response<DataArray<IndustryResponse>>> fetchIndustries(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("schools")
    Observable<Response<DataArray<SchoolResponse>>> fetchSchools(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("data/student_majors")
    Observable<Response<DataArray<MajorResponse>>> fetchStudentMajors(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("data/company_sizes")
    Observable<Response<DataArray<CompanySizeResponse>>> fetchCompanySizes();

    @GET("data/cities")
    Observable<Response<DataArray<LocationResponse>>> fetchLocations(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("data/job_skills")
    Observable<Response<DataArray<SkillResponse>>> fetchSkills(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("data/occupation_of_interests")
    Observable<Response<DataArray<OOIResponse>>> fetchOccupationOfInterests(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("interests")
    Observable<Response<DataArray<InterestsResponse>>> fetchInterests();

    @POST("job_employers")
    Observable<Response<Void>> createCompany(@Body CreateCompanyBody body);
}
