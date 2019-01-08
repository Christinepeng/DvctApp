package com.divercity.app.repository.registerlogin;

import com.divercity.app.data.entity.emailusernamecheck.CheckUsernameEmailResponse;
import com.divercity.app.data.entity.emailusernamecheck.emailbody.CheckEmailBody;
import com.divercity.app.data.entity.emailusernamecheck.usernamebody.CheckUsernameBody;
import com.divercity.app.data.entity.user.body.LoginBody;
import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.data.entity.signup.SignUpBody;
import com.divercity.app.data.networking.services.RegisterLoginService;
import com.divercity.app.repository.user.LoggedUserRepositoryImpl;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.Response;

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
    public Observable<UserResponse> login(String email, String password) {
        return registerLoginService.login(new LoginBody(email, password))
                .map(response -> {
                    checkResponse(response);
                    loggedUserRepository.saveUserHeaderData(response);
                    return response.body().getData();
                });
    }

    @Override
    public Observable<UserResponse> signUp(String nickname, String name, String email, String password, String confirmPassword) {
        return registerLoginService.signUp(new SignUpBody(password, confirmPassword, nickname, name, email))
                .map(response -> {
                    checkResponse(response);
                    loggedUserRepository.saveUserHeaderData(response);
                    return response.body().getData();
                });
    }

    @Override
    public Observable<UserResponse> loginLinkedin(String code, String state) {
        return registerLoginService.loginLinkedin(code, state).map(response -> {
            checkResponse(response);
            loggedUserRepository.saveUserHeaderData(response);
            return response.body().getData();
        });
    }

    @Override
    public Observable<UserResponse> loginFacebook(String token) {
        return registerLoginService.loginFacebook(RequestBody.create(MediaType.parse("text/plain"), token)).map(response -> {
            checkResponse(response);
            loggedUserRepository.saveUserHeaderData(response);
            return response.body().getData();
        });
    }

    @Override
    public Observable<CheckUsernameEmailResponse> isEmailRegistered(String email) {
        return registerLoginService.checkEmail(new CheckEmailBody(email));
    }

    @Override
    public Observable<CheckUsernameEmailResponse> isUsernameRegistered(String username) {
        return registerLoginService.checkUsername(new CheckUsernameBody(username));
    }

    private void checkResponse(Response response){
        if(!response.isSuccessful())
            throw new HttpException(response);
    }
}
