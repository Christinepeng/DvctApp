package com.divercity.app.features.login.step2;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.features.login.step2.usecase.LoginUseCase;

import javax.inject.Inject;

/**
 * Created by lucas on 25/09/2018.
 */

public class LoginViewModel extends BaseViewModel {

    private SingleLiveEvent<Resource<LoginResponse>> login = new SingleLiveEvent<>();
    private LoginUseCase loginUseCase;
    private Application application;
    private String userEmail;

    @Inject
    LoginViewModel(Application application,
                   LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public void login(String password) {
        if (password != null && !password.equals("")) {
            login.setValue(Resource.loading(null));
            LoginUseCase.Callback callback = new LoginUseCase.Callback() {
                @Override
                protected void onFail(String error) {
                    login.setValue(Resource.error(error, null));
                }

                @Override
                protected void onSuccess(LoginResponse response) {
                    login.setValue(Resource.success(response));
                }
            };
            getCompositeDisposable().add(callback);
            loginUseCase.execute(callback, LoginUseCase.Params.forLogin(userEmail, password));
        } else {
            login.setValue(Resource.error(application.getResources().getString(R.string.insert_valid_password), null));
        }
    }

    public LiveData<Resource<LoginResponse>> getLogin() {
        return login;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
