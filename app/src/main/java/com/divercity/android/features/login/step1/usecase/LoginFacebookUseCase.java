package com.divercity.android.features.login.step1.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 26/09/2018.
 */

public class LoginFacebookUseCase extends UseCase<UserResponse, LoginFacebookUseCase.Params> {

    private RegisterLoginRepository mRegisterLoginRepository;

    @Inject
    public LoginFacebookUseCase(@Named("executor_thread") Scheduler executorThread,
                                @Named("ui_thread") Scheduler uiThread,
                                RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        mRegisterLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<UserResponse> createObservableUseCase(Params params) {
        return mRegisterLoginRepository.loginFacebook(params.token);
    }

    public static final class Params {

        private final String token;

        private Params(String token) {
            this.token = token;
        }

        public static Params forFacebook(String token) {
            return new Params(token);
        }
    }
}
