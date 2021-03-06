package com.divercity.app.features.login.step1;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.core.utils.Util;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.login.step1.usecase.ChecIskEmailRegisteredUseCase;
import com.divercity.app.features.login.step1.usecase.ConnectLinkedInApiHelper;
import com.divercity.app.features.login.step1.usecase.LoginFacebookUseCase;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas on 26/09/2018.
 */

public class EnterEmailViewModel extends BaseViewModel {

    private ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase;
    private LoginFacebookUseCase loginFacebookUseCase;
    private Application application;
    private ConnectLinkedInApiHelper linkedInApiHelper;

    private SingleLiveEvent<Resource<Boolean>> isEmailRegistered = new SingleLiveEvent<>();
    private SingleLiveEvent<Resource<LoginResponse>> loginFacebookResponse = new SingleLiveEvent<>();

    private SingleLiveEvent<Object> navigateToSignUp = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToLogin = new SingleLiveEvent<>();

    @Inject
    public EnterEmailViewModel(Application application,
                               ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase,
                               ConnectLinkedInApiHelper linkedInApiHelper,
                               LoginFacebookUseCase loginFacebookUseCase) {
        this.application = application;
        this.loginFacebookUseCase = loginFacebookUseCase;
        this.checIskEmailRegisteredUseCase = checIskEmailRegisteredUseCase;
        this.linkedInApiHelper = linkedInApiHelper;
    }

    public void checkIfEmailRegistered(String email) {
        if (Util.isValidEmail(email.trim())) {
            isEmailRegistered.setValue(Resource.Companion.loading(null));
            DisposableObserver<Boolean> disposable = new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean r) {
                    isEmailRegistered.setValue(Resource.Companion.success(r));
                    if(r)
                        navigateToLogin.call();
                    else
                        navigateToSignUp.call();
                }

                @Override
                public void onError(Throwable e) {
                    isEmailRegistered.setValue(Resource.Companion.error(e.getMessage(),null));
                }

                @Override
                public void onComplete() {

                }
            };
            getCompositeDisposable().add(disposable);
            checIskEmailRegisteredUseCase.execute(disposable, ChecIskEmailRegisteredUseCase.Params.forCheckEmail(email));
        } else
            isEmailRegistered.setValue(Resource.Companion.error(application.getResources().getString(R.string.insert_valid_email), null));
    }

    public void loginFacebook(String token){
        loginFacebookResponse.setValue(Resource.Companion.loading(null));

        DisposableObserverWrapper<LoginResponse> disposable = new DisposableObserverWrapper<LoginResponse>() {
            @Override
            protected void onFail(@NotNull String error) {
                loginFacebookResponse.setValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                loginFacebookResponse.setValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(@NotNull LoginResponse loginResponse) {
                loginFacebookResponse.setValue(Resource.Companion.success(loginResponse));
            }
        };

        getCompositeDisposable().add(disposable);
        loginFacebookUseCase.execute(disposable, LoginFacebookUseCase.Params.forFacebook(token));
    }

    public MutableLiveData<Resource<Boolean>> getIsEmailRegistered() {
        return isEmailRegistered;
    }

    public SingleLiveEvent<Resource<LoginResponse>> getLoginFacebookResponse() {
        return loginFacebookResponse;
    }

    public SingleLiveEvent<Object> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public SingleLiveEvent<Object> getNavigateToLogin() {
        return navigateToLogin;
    }
}
