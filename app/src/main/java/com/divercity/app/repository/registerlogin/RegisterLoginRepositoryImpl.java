package com.divercity.app.repository.registerlogin;

import com.divercity.app.data.entity.emailcheck.CheckEmailBody;
import com.divercity.app.data.entity.emailcheck.CheckEmailResponse;
import com.divercity.app.data.entity.login.body.LoginBody;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.signup.body.SignUpBody;
import com.divercity.app.data.entity.signup.response.SignUpResponse;
import com.divercity.app.data.networking.services.RegisterLoginService;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by lucas on 09/09/2018.
 */

public class RegisterLoginRepositoryImpl implements RegisterLoginRepository {

    private UserSharedPreferencesRepository userSharedPreferencesRepository;

    private RegisterLoginService registerLoginService;

    @Inject
    public RegisterLoginRepositoryImpl(UserSharedPreferencesRepository userSharedPreferencesRepository,
                                       RegisterLoginService registerLoginService) {
        this.userSharedPreferencesRepository = userSharedPreferencesRepository;
        this.registerLoginService = registerLoginService;
    }

    @Override
    public Observable<Response<LoginResponse>> login(String email, String password) {
        return registerLoginService.login(new LoginBody(email, password)).doOnNext(response -> {
            if (response.isSuccessful()) {
                userSharedPreferencesRepository.setClient(response.headers().get("client"));
                userSharedPreferencesRepository.setUid(response.headers().get("uid"));
                userSharedPreferencesRepository.setAccessToken(response.headers().get("access-token"));
                userSharedPreferencesRepository.setAvatarUrl(response.body().getData().getAttributes().getAvatarThumb());
                userSharedPreferencesRepository.setUserId(response.body().getData().getId());
                userSharedPreferencesRepository.setAccountType(response.body().getData().getAttributes().getAccountType());
            }
        });
    }

    @Override
    public Observable<Response<SignUpResponse>> signUp(String nickname, String name, String email, String password, String confirmPassword) {
        return registerLoginService.signUp(new SignUpBody(password, confirmPassword, nickname, name, email));
    }

    @Override
    public Observable<CheckEmailResponse> isEmailRegistered(String email) {
        return registerLoginService.checkEmail(new CheckEmailBody(email));
    }
}
