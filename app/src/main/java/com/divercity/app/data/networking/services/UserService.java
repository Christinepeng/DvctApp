package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.login.response.LoginResponse;
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
}
