package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.Data;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lucas on 17/10/2018.
 */

public interface UserService {

    @GET("users/{id}")
    Observable<LoginResponse> fetchUserData(@Path("id") String userId);

    @GET("jobs/job_employers")
    Observable<Data<CompanyResponse>> fetchCompanies(@Query("page[number]") int pageNumber,
                                                     @Query("page[size]") int size,
                                                     @Query("search_query") String query);

    @GET("data/industries")
    Observable<Data<IndustryResponse>> fetchIndustries(@Query("page[number]") int pageNumber,
                                                       @Query("page[size]") int size,
                                                       @Query("search_query") String query);

    @GET("schools")
    Observable<Data<SchoolResponse>> fetchSchools(@Query("page[number]") int pageNumber,
                                                     @Query("page[size]") int size,
                                                     @Query("search_query") String query);

    @GET("data/student_majors")
    Observable<Data<MajorResponse>> fetchStudentMajors(@Query("page[number]") int pageNumber,
                                                 @Query("page[size]") int size,
                                                 @Query("search_query") String query);
}
