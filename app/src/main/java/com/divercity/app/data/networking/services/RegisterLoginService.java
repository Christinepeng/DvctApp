package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.emailusernamecheck.CheckUsernameEmailResponse;
import com.divercity.app.data.entity.emailusernamecheck.emailbody.CheckEmailBody;
import com.divercity.app.data.entity.emailusernamecheck.usernamebody.CheckUsernameBody;
import com.divercity.app.data.entity.login.body.LoginBody;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.signup.SignUpBody;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by lucas on 09/09/2018.
 */

public interface RegisterLoginService {

    @POST("auth/sign_in")
    Observable<Response<DataObject<LoginResponse>>> login(@Body LoginBody loginBody);

    @POST("users/check_email")
    Observable<CheckUsernameEmailResponse> checkEmail(@Body CheckEmailBody loginBody);

    @POST("users/check_username")
    Observable<CheckUsernameEmailResponse> checkUsername(@Body CheckUsernameBody loginBody);

    @POST("auth")
    Observable<Response<DataObject<LoginResponse>>> signUp(@Body SignUpBody loginBody);

    @GET("auth/sso/linkedin")
    Observable<Response<DataObject<LoginResponse>>> loginLinkedin(
            @Query("code") String code,
            @Query("state") String state);

    @Multipart
    @POST("auth/sso/facebook")
    Observable<Response<DataObject<LoginResponse>>> loginFacebook( @Part("token") RequestBody token);
}
