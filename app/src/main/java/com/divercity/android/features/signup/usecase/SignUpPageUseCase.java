package com.divercity.android.features.signup.usecase;

import com.divercity.android.core.base.usecase.UseCase;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.registerlogin.RegisterLoginRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 02/10/2018.
 */

public class SignUpPageUseCase extends UseCase<User, SignUpPageUseCase.Params> {

    private RegisterLoginRepository registerLoginRepository;

    @Inject
    public SignUpPageUseCase(@Named("executor_thread") Scheduler executorThread,
                             @Named("ui_thread") Scheduler uiThread,
                             RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        this.registerLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<User> createObservableUseCase(Params params) {
        return registerLoginRepository.signUpNew(params.name, params.email, params.password);
    }

    public static final class Params {
        String name;
        String email;
        String password;

        public Params(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public static Params forSignUp(String name, String email, String password) {
            return new Params( name, email, password);
        }
    }

    public static abstract class Callback extends DisposableObserverWrapper<User> {

        @Override
        protected void onHttpException(JsonElement error) {
            String strError = "";
            JsonArray array = error.getAsJsonObject().getAsJsonObject("errors")
                    .getAsJsonArray("full_messages");
            for(JsonElement element : array){
                if(strError.equals(""))
                    strError += element.toString();
                else
                    strError += "\n" + element.toString();
            }
            onFail(strError);
        }
    }
}
