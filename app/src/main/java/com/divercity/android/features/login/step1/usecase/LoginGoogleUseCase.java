package com.divercity.android.features.login.step1.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by Christine on 22/02/2020.
 */

public class LoginGoogleUseCase extends UseCase<User, LoginGoogleUseCase.Params> {

    private RegisterLoginRepository mRegisterLoginRepository;

    @Inject
    public LoginGoogleUseCase(@Named("executor_thread") Scheduler executorThread,
                              @Named("ui_thread") Scheduler uiThread,
                              RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        mRegisterLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(Params params) {
        return mRegisterLoginRepository.loginGoogle(params.token);
    }

    public static final class Params {

        private final String token;

        private Params(String token) {
            this.token = token;
        }

        public static Params forGoogle(String token) {
            return new Params(token);
        }
    }
}
