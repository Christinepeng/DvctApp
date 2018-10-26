package com.divercity.app.features.splash;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.features.splash.usecase.FetchCurrentUserDataUseCase;
import com.divercity.app.repository.user.LoggedUserRepositoryImpl;

import javax.inject.Inject;

public class SplashViewModel extends BaseViewModel {

    private SingleLiveEvent<Resource<LoginResponse>> userData = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> navigateToEnterEmail = new SingleLiveEvent<>();
    private SingleLiveEvent<Boolean> navigateToHome = new SingleLiveEvent<>();
    private FetchCurrentUserDataUseCase fetchCurrentUserDataUseCase;
    private LoggedUserRepositoryImpl loggedUserRepository;

    @Inject
    SplashViewModel(FetchCurrentUserDataUseCase fetchCurrentUserDataUseCase,
                    LoggedUserRepositoryImpl loggedUserRepository) {
        this.fetchCurrentUserDataUseCase = fetchCurrentUserDataUseCase;
        this.loggedUserRepository = loggedUserRepository;
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
                    navigateToEnterEmail.setValue(true);
                else
                    navigateToHome.setValue(true);
            }
        };
        getCompositeDisposable().add(callback);
        fetchCurrentUserDataUseCase.execute(callback, null);
    }

    public boolean isUserLogged(){
        return loggedUserRepository.isUserLogged();
    }

    public SingleLiveEvent<Resource<LoginResponse>> getUserData() {
        return userData;
    }

    public SingleLiveEvent<Boolean> getNavigateToEnterEmail() {
        return navigateToEnterEmail;
    }

    public SingleLiveEvent<Boolean> getNavigateToHome() {
        return navigateToHome;
    }
}