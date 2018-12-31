package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.industry.body.FollowIndustryBody;
import com.divercity.app.data.entity.interests.body.FollowInterestsBody;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.occupationofinterests.body.FollowOOIBody;
import com.divercity.app.data.entity.profile.picture.ProfilePictureBody;
import com.divercity.app.data.entity.profile.profile.UserProfileBody;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * Created by lucas on 17/10/2018.
 */

public interface UserService {

    @GET("users/{id}")
    Observable<Response<DataObject<LoginResponse>>> fetchUserData(@Path("id") String userId);

    @PUT("users/{id}")
    Observable<Response<DataObject<LoginResponse>>> updateUserProfile(@Path("id") String userId, @Body UserProfileBody body);

    @POST("group_of_interests/{id}/follow")
    Observable<Response<Void>> joinGroup(@Path("id") String idGroup);

    @POST("users/avatar_upload")
    Observable<Response<DataObject<LoginResponse>>> uploadUserPhoto(@Body ProfilePictureBody body);

    @POST("data/follow_occupation_of_interest")
    Observable<Response<DataObject<LoginResponse>>> followOccupationOfInterests(@Body FollowOOIBody body);

    @POST("users/current/update_interests")
    Observable<Response<DataObject<LoginResponse>>> followInterests(@Body FollowInterestsBody body);

    @POST("data/follow_industry")
    Observable<Response<DataObject<LoginResponse>>> followIndustries(@Body FollowIndustryBody body);

    @GET("users/{id}/followers")
    Observable<Response<DataArray<LoginResponse>>> fetchFollowersByUser(
            @Path("id") String userId,
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("users/{id}/following")
    Observable<Response<DataArray<LoginResponse>>> fetchFollowingByUser(
            @Path("id") String userId,
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("users?order_by_name=asc")
    Observable<Response<DataArray<LoginResponse>>> fetchUsers(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);
}
