package com.divercity.app.features.login.step1;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.core.utils.Util;
import com.divercity.app.data.Resource;
import com.divercity.app.features.login.step1.usecase.ChecIskEmailRegisteredUseCase;
import com.divercity.app.features.login.step1.usecase.ConnectLinkedInApiHelper;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas on 26/09/2018.
 */

public class EnterEmailViewModel extends BaseViewModel {

    private ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase;
    private Application application;
    private ConnectLinkedInApiHelper linkedInApiHelper;

    private SingleLiveEvent<Resource<Boolean>> isEmailRegistered = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToSignUp = new SingleLiveEvent<>();
    private SingleLiveEvent<Object> navigateToLogin = new SingleLiveEvent<>();

    @Inject
    public EnterEmailViewModel(Application application,
                               ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase,
                               ConnectLinkedInApiHelper linkedInApiHelper) {
        this.application = application;
        this.checIskEmailRegisteredUseCase = checIskEmailRegisteredUseCase;
        this.linkedInApiHelper = linkedInApiHelper;
    }

    public void checkIfEmailRegistered(String email) {
        if (Util.isValidEmail(email)) {
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

    public MutableLiveData<Resource<Boolean>> getIsEmailRegistered() {
        return isEmailRegistered;
    }

    public SingleLiveEvent<Object> getNavigateToSignUp() {
        return navigateToSignUp;
    }

    public SingleLiveEvent<Object> getNavigateToLogin() {
        return navigateToLogin;
    }

//    public void getLinkedInToken(FragmentActivity activity){
////        linkedInApiHelper.getLinkedInToken(activity, new ConnectLinkedInApiHelper.Listener() {
////            @Override
////            public void onAuthSucces(@Nullable AccessToken token) {
////                String hola = token.getValue();
////            }
////
////            @Override
////            public void onAuthError(@NotNull String msg) {
////                String errror = msg;
////            }
////        });
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
////        linkedInApiHelper.onActivityResult(requestCode, resultCode, data);
//    }
}
