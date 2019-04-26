package com.divercity.android.features.onboarding.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.entity.profile.profile.UserProfileEntity;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.user.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class UpdateUserProfileUseCase extends UseCase<User, UpdateUserProfileUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public UpdateUserProfileUseCase(@Named("executor_thread") Scheduler executorThread,
                                    @Named("ui_thread") Scheduler uiThread,
                                    UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(UpdateUserProfileUseCase.Params params) {
        return userRepository.updateLoggedUserProfile(params.user);
    }

    public static final class Params {

        private final UserProfileEntity user;

        private Params(UserProfileEntity user) {
            this.user = user;
        }

        public static UpdateUserProfileUseCase.Params forUser(UserProfileEntity user) {
            return new UpdateUserProfileUseCase.Params(user);
        }
    }
}
