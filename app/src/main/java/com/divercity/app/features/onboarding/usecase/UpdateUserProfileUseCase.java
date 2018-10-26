package com.divercity.app.features.onboarding.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class UpdateUserProfileUseCase extends UseCase<LoginResponse, UpdateUserProfileUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public UpdateUserProfileUseCase(@Named("executor_thread") Scheduler executorThread,
                                    @Named("ui_thread") Scheduler uiThread,
                                    UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<LoginResponse> createObservableUseCase(UpdateUserProfileUseCase.Params params) {
        return userRepository.updateUserProfile(params.user);
    }

    public static final class Params {

        private final User user;

        private Params(User user) {
            this.user = user;
        }

        public static UpdateUserProfileUseCase.Params forUser(User user) {
            return new UpdateUserProfileUseCase.Params(user);
        }
    }
}
