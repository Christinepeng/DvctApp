package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.industry.body.FollowIndustryBody;
import com.divercity.app.data.entity.interests.body.FollowInterestsBody;
import com.divercity.app.data.entity.user.response.UserResponse;
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
    Observable<Response<DataObject<UserResponse>>> fetchUserData(@Path("id") String userId);

    @PUT("users/{id}")
    Observable<Response<DataObject<UserResponse>>> updateUserProfile(@Path("id") String userId, @Body UserProfileBody body);

    @POST("group_of_interests/{id}/follow")
    Observable<Response<Void>> joinGroup(@Path("id") String idGroup);

    @POST("users/avatar_upload")
    Observable<Response<DataObject<UserResponse>>> uploadUserPhoto(@Body ProfilePictureBody body);

    @POST("data/follow_occupation_of_interest")
    Observable<Response<DataObject<UserResponse>>> followOccupationOfInterests(@Body FollowOOIBody body);

    @POST("users/current/update_interests")
    Observable<Response<DataObject<UserResponse>>> followInterests(@Body FollowInterestsBody body);

    @POST("data/follow_industry")
    Observable<Response<DataObject<UserResponse>>> followIndustries(@Body FollowIndustryBody body);

    @GET("users/{id}/followers")
    Observable<Response<DataArray<UserResponse>>> fetchFollowersByUser(
            @Path("id") String userId,
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("users/{id}/following")
    Observable<Response<DataArray<UserResponse>>> fetchFollowingByUser(
            @Path("id") String userId,
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);

    @GET("users?order_by_name=asc")
    Observable<Response<DataArray<UserResponse>>> fetchUsers(
            @Query("page[number]") int pageNumber,
            @Query("page[size]") int size,
            @Query("search_query") String query);
}
