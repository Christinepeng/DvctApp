package com.divercity.app.features.login.step1;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.core.utils.Util;
import com.divercity.app.data.Resource;
import com.divercity.app.features.login.step1.usecase.ChecIskEmailRegisteredUseCase;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas on 26/09/2018.
 */

public class EnterEmailViewModel extends BaseViewModel {

    private ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase;
    private Application application;

    private SingleLiveEvent<Resource<Boolean>> isEmailRegistered = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToSignUp = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToLogin = new SingleLiveEvent<>();

    @Inject
    public EnterEmailViewModel(Application application,
                               ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase) {
        this.application = application;
        this.checIskEmailRegisteredUseCase = checIskEmailRegisteredUseCase;
    }

    public void checkIfEmailRegistered(String email) {
        if (Util.isValidEmail(email)) {
            isEmailRegistered.setValue(Resource.loading(null));
            DisposableObserver<Boolean> disposable = new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean r) {
                    isEmailRegistered.setValue(Resource.success(r));
                    if(r)
                        navigateToLogin.call();
                    else
                        navigateToSignUp.call();
                }

                @Override
                public void onError(Throwable e) {
                    isEmailRegistered.setValue(Resource.error(e.getMessage(),null));
                }

                @Override
                public void onComplete() {

                }
            };
            getCompositeDisposable().add(disposable);
            checIskEmailRegisteredUseCase.execute(disposable, ChecIskEmailRegisteredUseCase.Params.forCheckEmail(email));
        } else
            isEmailRegistered.setValue(Resource.error(application.getResources().getString(R.string.insert_valid_email), null));
    }

    public MutableLiveData<Resource<Boolean>> getIsEmailRegistered() {
        return isEmailRegistered;
    }

    public SingleLiveEvent<Object> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public SingleLiveEvent<Object> getNavigateToLogin() {
        return navigateToLogin;
    }
}
