package com.divercity.app.data.networking.services;

import com.divercity.app.data.entity.emailcheck.CheckEmailBody;
import com.divercity.app.data.entity.emailcheck.CheckEmailResponse;
import com.divercity.app.data.entity.login.body.LoginBody;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.signup.body.SignUpBody;
import com.divercity.app.data.entity.signup.response.SignUpResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by lucas on 09/09/2018.
 */

public interface RegisterLoginService {

    @POST("auth/sign_in")
    Observable<Response<LoginResponse>> login(@Body LoginBody loginBody);

    @POST("users/check_email")
    Observable<CheckEmailResponse> checkEmail(@Body CheckEmailBody loginBody);

    @POST("auth")
    Observable<Response<SignUpResponse>> signUp(@Body SignUpBody loginBody);
}
