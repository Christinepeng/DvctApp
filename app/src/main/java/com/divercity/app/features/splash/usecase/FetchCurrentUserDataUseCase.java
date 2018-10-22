package com.divercity.app.features.splash.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class FetchCurrentUserDataUseCase extends UseCase<LoginResponse, FetchCurrentUserDataUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public FetchCurrentUserDataUseCase(@Named("executor_thread") Scheduler executorThread,
                                       @Named("ui_thread") Scheduler uiThread,
                                       UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<LoginResponse> createObservableUseCase(Params params) {
        return userRepository.fetchUserData(userRepository.getCurrentUserId());
    }

    public static final class Params {}

    public static abstract class Callback extends DisposableObserverWrapper<LoginResponse> {

        @Override
        protected void onHttpException(JsonElement error) {
            onFail(error.getAsJsonObject().getAsJsonArray("errors").get(0).getAsString());
        }
    }
}
