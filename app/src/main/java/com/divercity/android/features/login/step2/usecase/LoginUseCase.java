package com.divercity.android.features.login.step2.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.networking.config.DisposableUnauthObserverWrapper;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class LoginUseCase extends UseCase<User, LoginUseCase.Params> {

    private RegisterLoginRepository registerLoginRepository;

    @Inject
    public LoginUseCase(@Named("executor_thread") Scheduler executorThread,
                        @Named("ui_thread") Scheduler uiThread,
                        RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        this.registerLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(Params params) {
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

    public static abstract class Callback extends DisposableUnauthObserverWrapper<User> {

        @Override
        protected void onHttpException(JsonElement error) {
            onFail(error.getAsJsonObject().getAsJsonArray("errors").get(0).getAsString());
        }
    }
}
