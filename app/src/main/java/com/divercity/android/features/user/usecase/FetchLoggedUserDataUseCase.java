package com.divercity.android.features.user.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class FetchLoggedUserDataUseCase extends UseCase<User, Object> {

    private UserRepository userRepository;

    @Inject
    public FetchLoggedUserDataUseCase(@Named("executor_thread") Scheduler executorThread,
                                      @Named("ui_thread") Scheduler uiThread,
                                      UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(Object v) {
        return userRepository.fetchLoggedUserData();
    }

    public static abstract class Callback extends DisposableObserverWrapper<User> {

        @Override
        protected void onHttpException(JsonElement error) {
            onFail(error.getAsJsonObject().getAsJsonArray("errors").get(0).getAsString());
        }
    }
}
