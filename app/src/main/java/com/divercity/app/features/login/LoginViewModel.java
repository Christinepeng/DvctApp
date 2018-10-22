package com.divercity.app.features.login;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.features.login.usecase.LoginUseCase;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by lucas on 25/09/2018.
 */

public class LoginViewModel extends BaseViewModel {

    private SingleLiveEvent<Resource<LoginResponse>> login = new SingleLiveEvent<>();
    public ObservableField<String> userPassword = new ObservableField<>();
    private LoginUseCase loginUseCase;
    private String userEmail;

    @Inject
    LoginViewModel(@NonNull Application application,
                          LoginUseCase loginUseCase) {
        super(application);
        this.loginUseCase = loginUseCase;
    }

    public void login(String password) {
        if(password != null && !password.equals("")) {
            login.setValue(Resource.loading(null));
            LoginUseCase.Callback callback = new LoginUseCase.Callback() {
                @Override
                protected void onFail(String error) {
                    login.setValue(Resource.error(error, null));
                }

                @Override
                protected void onSuccess(Response<LoginResponse> response) {
                    login.setValue(Resource.success(response.body()));
                }


//                @Override
//                protected void onFail(String msg) {
//                    login.setValue(Resource.error(msg, null));
//                }
//
//                @Override
//                protected void onSuccess(LoginResponse loginResponse) {
//                    login.setValue(Resource.success(loginResponse));
//                }

            };
            getCompositeDisposable().add(callback);
            loginUseCase.execute(callback, LoginUseCase.Params.forLogin(userEmail, password));
        } else {
            login.setValue(Resource.error(getApplication().getResources().getString(R.string.insert_valid_password), null));
        }
    }

    public LiveData<Resource<LoginResponse>> getLogin() {
        return login;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

}
