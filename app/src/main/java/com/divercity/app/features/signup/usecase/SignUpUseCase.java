package com.divercity.app.features.signup.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.repository.registerlogin.RegisterLoginRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 02/10/2018.
 */

public class SignUpUseCase extends UseCase<LoginResponse, SignUpUseCase.Params> {

    private RegisterLoginRepository registerLoginRepository;

    @Inject
    public SignUpUseCase(@Named("executor_thread") Scheduler executorThread,
                         @Named("ui_thread") Scheduler uiThread,
                         RegisterLoginRepository registerLoginRepository) {
        super(executorThread, uiThread);
        this.registerLoginRepository = registerLoginRepository;
    }

    @Override
    protected Observable<LoginResponse> createObservableUseCase(Params params) {
        return registerLoginRepository.signUp(params.nickname, params.name, params.email, params.password, params.confirmPassword);
    }

    public static final class Params {

        String nickname;
        String name;
        String email;
        String password;
        String confirmPassword;

        public Params(String nickname, String name, String email, String password, String confirmPassword) {
            this.nickname = nickname;
            this.name = name;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public static Params forSignUp(String nickname, String name, String email, String password, String confirmPassword) {
            return new Params(nickname, name, email, password, confirmPassword);
        }
    }

    public static abstract class Callback extends DisposableObserverWrapper<LoginResponse> {

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
