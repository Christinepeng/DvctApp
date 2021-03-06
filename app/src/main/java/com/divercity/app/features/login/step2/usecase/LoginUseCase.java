package com.divercity.app.features.login.step2.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.repository.registerlogin.RegisterLoginRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class LoginUseCase extends UseCase<LoginResponse, LoginUseCase.Params> {

    private RegisterLoginRepository registerLoginRepository;

    @Inject
    public LoginUseCase(@Named("executor_thread") Scheduler executorThread,
                        @Named("ui_thread") Scheduler uiThread,
                        RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        this.registerLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<LoginResponse> createObservableUseCase(Params params) {
        return registerLoginRepository.login(params.email, params.password);
    }

    public static final class Params {

        private final String email;
        private final String password;

        private Params(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public static Params forLogin(String email, String password) {
            return new Params(email, password);
        }
    }

    public static abstract class Callback extends DisposableObserverWrapper<LoginResponse> {

        @Override
        protected void onHttpException(JsonElement error) {
            onFail(error.getAsJsonObject().getAsJsonArray("errors").get(0).getAsString());
        }
    }
}
