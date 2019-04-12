package com.divercity.android.features.signup.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 26/09/2018.
 */

public class CheckIsUsernameRegisteredUseCase extends UseCase<Boolean, CheckIsUsernameRegisteredUseCase.Params> {

    private RegisterLoginRepository mRegisterLoginRepository;

    @Inject
    public CheckIsUsernameRegisteredUseCase(@Named("executor_thread") Scheduler executorThread,
                                            @Named("ui_thread") Scheduler uiThread,
                                            RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        mRegisterLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<Boolean> createObservableUseCase(Params params) {
        return mRegisterLoginRepository.isEmailRegistered(params.username).map(checkUsernameEmailResponse ->
                !checkUsernameEmailResponse.getStatus().equals("free")
        );
    }

    public static final class Params {

        private final String username;

        private Params(String username) {
            this.username = username;
        }

        public static Params forCheckUsername(String username) {
            return new Params(username);
        }
    }
}
