package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lucas on 09/09/2018.
 */

public interface DataService {

    @GET("jobs/job_employers")
    Observable<DataArray<CompanyResponse>> fetchCompanies(@Query("page[number]") int pageNumber,
                                                          @Query("page[size]") int size,
                                                          @Query("search_query") String query);

    @GET("data/industries")
    Observable<DataArray<IndustryResponse>> fetchIndustries(@Query("page[number]") int pageNumber,
                                                            @Query("page[size]") int size,
                                                            @Query("search_query") String query);

    @GET("schools")
    Observable<DataArray<SchoolResponse>> fetchSchools(@Query("page[number]") int pageNumber,
                                                       @Query("page[size]") int size,
                                                       @Query("search_query") String query);

    @GET("data/student_majors")
    Observable<DataArray<MajorResponse>> fetchStudentMajors(@Query("page[number]") int pageNumber,
                                                            @Query("page[size]") int size,
                                                            @Query("search_query") String query);

    @GET("group_of_interests/onboarding")
    Observable<DataArray<GroupResponse>> fetchGroups(@Query("page[number]") int pageNumber,
                                                     @Query("page[size]") int size,
                                                     @Query("search_query") String query);

    @GET("group_of_interests/following")
    Observable<DataArray<GroupResponse>> fetchFollowedGroups(@Query("page[number]") int pageNumber,
                                                     @Query("page[size]") int size,
                                                     @Query("search_query") String query);

    @GET("data/cities")
    Observable<DataArray<LocationResponse>> fetchLocations(@Query("page[number]") int pageNumber,
                                                           @Query("page[size]") int size,
                                                           @Query("search_query") String query);
}
