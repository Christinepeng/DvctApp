package com.divercity.app.repository.registerlogin;

import com.divercity.app.data.entity.emailcheck.CheckEmailBody;
import com.divercity.app.data.entity.emailcheck.CheckEmailResponse;
import com.divercity.app.data.entity.login.body.LoginBody;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.signup.body.SignUpBody;
import com.divercity.app.data.entity.signup.response.SignUpResponse;
import com.divercity.app.data.networking.services.RegisterLoginService;
import com.divercity.app.repository.user.LoggedUserRepositoryImpl;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by lucas on 09/09/2018.
 */

public class RegisterLoginRepositoryImpl implements RegisterLoginRepository {

    private LoggedUserRepositoryImpl loggedUserRepository;

    private RegisterLoginService registerLoginService;

    @Inject
    public RegisterLoginRepositoryImpl(LoggedUserRepositoryImpl loggedUserRepository,
                                       RegisterLoginService registerLoginService) {
        this.loggedUserRepository = loggedUserRepository;
        this.registerLoginService = registerLoginService;
    }

    @Override
    public Observable<LoginResponse> login(String email, String password) {
        return registerLoginService.login(new LoginBody(email, password))
                .map(response -> {
                    if(response.isSuccessful()){
                        loggedUserRepository.setClient(response.headers().get("client"));
                        loggedUserRepository.setUid(response.headers().get("uid"));
                        loggedUserRepository.setAccessToken(response.headers().get("access-token"));
                        loggedUserRepository.setAvatarUrl(response.body().getData().getAttributes().getAvatarThumb());
                        loggedUserRepository.setUserId(response.body().getData().getId());
                        loggedUserRepository.setAccountType(response.body().getData().getAttributes().getAccountType());
                    }
                    return response.body().getData();
                });
    }

    @Override
    public Observable<SignUpResponse> signUp(String nickname, String name, String email, String password, String confirmPassword) {
        return registerLoginService.signUp(new SignUpBody(password, confirmPassword, nickname, name, email))
                .map(response -> {
                    if(response.isSuccessful()){
                        loggedUserRepository.setClient(response.headers().get("client"));
                        loggedUserRepository.setUid(response.headers().get("uid"));
                        loggedUserRepository.setAccessToken(response.headers().get("access-token"));
                        loggedUserRepository.setAvatarUrl(response.body().getData().getAttributes().getAvatarThumb());
                    }
                    return response.body();
                });
    }

    @Override
    public Observable<CheckEmailResponse> isEmailRegistered(String email) {
        return registerLoginService.checkEmail(new CheckEmailBody(email));
    }
}
