package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.profile.UserProfileBody;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lucas on 17/10/2018.
 */

public interface UserService {

    @GET("users/{id}")
    Observable<DataObject<LoginResponse>> fetchUserData(@Path("id") String userId);

    @PUT("users/{id}")
    Observable<DataObject<LoginResponse>> updateUserProfile(@Path("id") String userId, @Body UserProfileBody body);

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

    @POST("group_of_interests/{id}/follow")
    Observable<Response<Void>> joinGroup(@Path("id") String idGroup);
}
