package com.divercity.android.features.login.step2;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.divercity.android.R;
import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.utils.SingleLiveEvent;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.login.step2.usecase.LoginUseCase;

import javax.inject.Inject;

/**
 * Created by lucas on 25/09/2018.
 */

public class LoginViewModel extends BaseViewModel {

    private SingleLiveEvent<Resource<UserResponse>> login = new SingleLiveEvent<>();
    private LoginUseCase loginUseCase;
    private Application application;
    private String userEmail;

    @Inject
    LoginViewModel(Application application,
                   LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
        this.application = application;
    }

    public void login(String password) {
        if (password != null && !password.equals("")) {
            login.setValue(Resource.Companion.loading(null));
            LoginUseCase.Callback callback = new LoginUseCase.Callback() {
                @Override
                protected void onFail(String error) {
                    login.setValue(Resource.Companion.error(error, null));
                }

                @Override
                protected void onSuccess(UserResponse response) {
                    login.setValue(Resource.Companion.success(response));
                }
            };
            loginUseCase.execute(callback, LoginUseCase.Params.forLogin(userEmail, password));
        } else {
            login.setValue(Resource.Companion.error(application.getResources().getString(R.string.insert_valid_password), null));
        }
    }

    public LiveData<Resource<UserResponse>> getLogin() {
        return login;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        loginUseCase.dispose();
    }
}
