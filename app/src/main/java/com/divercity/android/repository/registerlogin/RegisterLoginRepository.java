package com.divercity.android.repository.registerlogin;

import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse;
import com.divercity.android.data.entity.user.response.UserResponse;

import io.reactivex.Observable;

/**
 * Created by lucas on 09/09/2018.
 */

public interface RegisterLoginRepository {

    Observable<UserResponse> login(String email, String password);

    Observable<CheckUsernameEmailResponse> isEmailRegistered(String email);

    Observable<CheckUsernameEmailResponse> isUsernameRegistered(String username);

    Observable<UserResponse> signUp(String nickname,
                                    String name,
                                    String email,
                                    String password,
                                    String confirmPassword);

    Observable<UserResponse> loginLinkedin(
            String code,
            String state);

    Observable<UserResponse> loginFacebook(String token);
}
