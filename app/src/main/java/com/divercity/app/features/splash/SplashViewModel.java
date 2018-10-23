package com.divercity.app.features.splash;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.features.login.usecase.LoginUseCase;
import com.divercity.app.features.splash.usecase.FetchCurrentUserDataUseCase;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SplashViewModel extends BaseViewModel {

    LoginUseCase loginUseCase;
    public SingleLiveEvent<Resource<LoginResponse>> userData = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> navigateToSelectUserType = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> navigateToHome = new SingleLiveEvent<>();
    private FetchCurrentUserDataUseCase fetchCurrentUserDataUseCase;

    @Inject
    SplashViewModel(@NonNull Application application,
                    FetchCurrentUserDataUseCase fetchCurrentUserDataUseCase) {
        super(application);
        this.fetchCurrentUserDataUseCase = fetchCurrentUserDataUseCase;
    }

    public void fetchCurrentUserData() {
        userData.setValue(Resource.loading(null));
        FetchCurrentUserDataUseCase.Callback callback = new FetchCurrentUserDataUseCase.Callback() {
            @Override
            protected void onFail(String error) {
                userData.setValue(Resource.error(error, null));
            }

            @Override
            protected void onSuccess(LoginResponse response) {
                userData.setValue(Resource.success(response));
                if (response.getAttributes().getAccountType() == null)
                    navigateToSelectUserType.setValue(true);
                else
                    navigateToHome.setValue(true);
            }
        };
        getCompositeDisposable().add(callback);
        fetchCurrentUserDataUseCase.execute(callback, null);


//        getCurrentUserDataUseCase.execute(new DisposableObserver<LoginResponse>() {
//            @Override
//            public void onNext(LoginResponse loginResponse) {
//                userData.setValue(Resource.success(loginResponse));
//                if(loginResponse.getData().getAttributes().getAccountType() == null)
//                    navigateToSelectUserType.setValue(true);
//                else
//                    navigateToHome.setValue(true);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                userData.setValue(Resource.error(e.getMessage(), null));
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        }, null);
    }

}
