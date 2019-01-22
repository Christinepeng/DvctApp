package com.divercity.android.data.networking.services;

import com.divercity.android.data.entity.base.DataArray;
import com.divercity.android.data.entity.base.DataObject;
import com.divercity.android.data.entity.industry.body.FollowIndustryBody;
import com.divercity.android.data.entity.interests.body.FollowInterestsBody;
import com.divercity.android.data.entity.occupationofinterests.body.FollowOOIBody;
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody;
import com.divercity.android.data.entity.profile.profile.UserProfileBody;
import com.divercity.android.data.entity.user.followuser.FollowUserResponse;
import com.divercity.android.data.entity.user.response.UserResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @Multipart
    @POST("users/follow")
    Observable<Response<DataObject<FollowUserResponse>>> followUser(@Part("user_id")RequestBody userId);

    @Multipart
    @DELETE("users/unfollow")
    Observable<Response<Void>> unfollowUser(@Part("user_id")RequestBody userId);

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
