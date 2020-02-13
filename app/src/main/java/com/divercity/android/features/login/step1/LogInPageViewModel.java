package com.divercity.android.features.login.step1;

import android.app.Application;

import com.divercity.android.R;
import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.core.utils.SingleLiveEvent;
import com.divercity.android.core.utils.Util;
import com.divercity.android.data.Resource;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.login.step1.usecase.ChecIskEmailRegisteredUseCase;
import com.divercity.android.features.login.step1.usecase.ConnectLinkedInApiHelper;
import com.divercity.android.features.login.step1.usecase.LoginFacebookUseCase;
import com.divercity.android.model.user.User;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas on 26/09/2018.
 */

public class LogInPageViewModel extends BaseViewModel {

    private ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase;
    private LoginFacebookUseCase loginFacebookUseCase;
    private Application application;
    private ConnectLinkedInApiHelper linkedInApiHelper;

    private SingleLiveEvent<Resource<Boolean>> isEmailRegistered = new SingleLiveEvent<>();
    private SingleLiveEvent<Resource<User>> loginFacebookResponse = new SingleLiveEvent<>();

    private SingleLiveEvent<Object> navigateToSignUp = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToLogin = new SingleLiveEvent<>();

    @Inject
    public LogInPageViewModel(Application application,
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
            checIskEmailRegisteredUseCase.execute(disposable, ChecIskEmailRegisteredUseCase.Params.forCheckEmail(email));
        } else
            isEmailRegistered.setValue(Resource.Companion.error(application.getResources().getString(R.string.insert_valid_email), null));
    }

    public void loginFacebook(String token) {
        loginFacebookResponse.setValue(Resource.Companion.loading(null));

        DisposableObserverWrapper<User> disposable = new DisposableObserverWrapper<User>() {
            @Override
            protected void onFail(@NotNull String error) {
                loginFacebookResponse.setValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                loginFacebookResponse.setValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(@NotNull User loginResponse) {
                loginFacebookResponse.setValue(Resource.Companion.success(loginResponse));
            }
        };

        loginFacebookUseCase.execute(disposable, LoginFacebookUseCase.Params.forFacebook(token));
    }

    public void loginGoogle(String token) {
        // TODO
    }

    public MutableLiveData<Resource<Boolean>> getIsEmailRegistered() {
        return isEmailRegistered;
    }

    public SingleLiveEvent<Resource<User>> getLoginFacebookResponse() {
        return loginFacebookResponse;
    }

    public SingleLiveEvent<Object> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public SingleLiveEvent<Object> getNavigateToLogin() {
        return navigateToLogin;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        loginFacebookUseCase.dispose();
        checIskEmailRegisteredUseCase.dispose();
    }
}
