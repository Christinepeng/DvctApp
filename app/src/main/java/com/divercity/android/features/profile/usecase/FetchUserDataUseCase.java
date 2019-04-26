package com.divercity.android.features.profile.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class FetchUserDataUseCase extends UseCase<User, FetchUserDataUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public FetchUserDataUseCase(@Named("executor_thread") Scheduler executorThread,
                                @Named("ui_thread") Scheduler uiThread,
                                UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(Params params) {
        return userRepository.fetchRemoteUserData(params.userId);
    }

    public static final class Params {
        private final String userId;

        private Params(String userId) {
            this.userId = userId;
        }

        public static Params forUserData(String userId) {
            return new Params(userId);
        }
    }

    public static abstract class Callback extends DisposableObserverWrapper<User> {

        @Override
        protected void onHttpException(JsonElement error) {
            onFail(error.getAsJsonObject().getAsJsonArray("errors").get(0).getAsString());
        }
    }
}
