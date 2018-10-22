package com.divercity.app.repository.registerlogin;

import com.divercity.app.data.entity.emailcheck.CheckEmailResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.signup.response.SignUpResponse;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by lucas on 09/09/2018.
 */

public interface RegisterLoginRepository {

    Observable<Response<LoginResponse>> login(String email, String password);

    Observable<CheckEmailResponse> isEmailRegistered(String email);

    Observable<Response<SignUpResponse>> signUp(String nickname,
                                      String name,
                                      String email,
                                      String password,
                                      String confirmPassword);
}
