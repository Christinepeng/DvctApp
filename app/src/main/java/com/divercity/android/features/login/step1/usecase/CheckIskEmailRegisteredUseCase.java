package com.divercity.android.features.login.step1.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 26/09/2018.
 */

public class CheckIskEmailRegisteredUseCase extends UseCase<Boolean, CheckIskEmailRegisteredUseCase.Params> {

    private RegisterLoginRepository mRegisterLoginRepository;

    @Inject
    public CheckIskEmailRegisteredUseCase(@Named("executor_thread") Scheduler executorThread,
                                         @Named("ui_thread") Scheduler uiThread,
                                         RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        mRegisterLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<Boolean> createObservableUseCase(Params params) {
        return mRegisterLoginRepository.isEmailRegistered(params.email).map(checkEmailResponse ->
                !checkEmailResponse.getStatus().equals("free")
        );
    }

    public static final class Params {

        private final String email;

        private Params(String email) {
            this.email = email;
        }

        public static Params forCheckEmail(String email) {
            return new Params(email);
        }
    }
}
