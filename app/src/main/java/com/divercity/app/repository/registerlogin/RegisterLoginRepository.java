package com.divercity.app.repository.registerlogin;

import com.divercity.app.data.entity.emailusernamecheck.CheckUsernameEmailResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;

import io.reactivex.Observable;

/**
 * Created by lucas on 09/09/2018.
 */

public interface RegisterLoginRepository {

    Observable<LoginResponse> login(String email, String password);

    Observable<CheckUsernameEmailResponse> isEmailRegistered(String email);

    Observable<CheckUsernameEmailResponse> isUsernameRegistered(String username);

    Observable<LoginResponse> signUp(String nickname,
                                     String name,
                                     String email,
                                     String password,
                                     String confirmPassword);

    Observable<LoginResponse> loginLinkedin(
            String code,
            String state);
}
