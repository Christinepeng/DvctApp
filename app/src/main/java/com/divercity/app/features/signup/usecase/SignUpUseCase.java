package com.divercity.app.features.signup.usecase;

import com.divercity.app.repository.registerlogin.RegisterLoginRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.signup.response.SignUpResponse;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by lucas on 02/10/2018.
 */

public class SignUpUseCase extends UseCase<Response<SignUpResponse>, SignUpUseCase.Params> {

    private RegisterLoginRepository registerLoginRepository;
    static private UserSharedPreferencesRepository userSharedPreferencesRepository;

    @Inject
    public SignUpUseCase(@Named("executor_thread") Scheduler executorThread,
                         @Named("ui_thread") Scheduler uiThread,
                         RegisterLoginRepository registerLoginRepository,
                         UserSharedPreferencesRepository userSharedPreferencesRepository) {
        super(executorThread, uiThread);
        this.registerLoginRepository = registerLoginRepository;
        this.userSharedPreferencesRepository = userSharedPreferencesRepository;
    }

    @Override
    protected Observable<Response<SignUpResponse>> createObservableUseCase(Params params) {
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

    public static abstract class SignUpCaseCallback extends DisposableObserver<Response<SignUpResponse>> {

        protected abstract void onFail(String msg);

        protected abstract void onSuccess(SignUpResponse loginResponse);

        @Override
        public void onNext(Response<SignUpResponse> response) {
            if (response.code() != 200) {
                onError(new HttpException(response));
            } else {
                userSharedPreferencesRepository.setClient(response.headers().get("client"));
                userSharedPreferencesRepository.setUid(response.headers().get("uid"));
                userSharedPreferencesRepository.setAccessToken(response.headers().get("access-token"));
                userSharedPreferencesRepository.setAvatarUrl(response.body().getData().getAttributes().getAvatarThumb());
                onSuccess(response.body());
            }
        }

        @Override
        public void onError(Throwable t) {
            if (t instanceof HttpException) {
                ResponseBody responseBody = ((HttpException) t).response().errorBody();
                try {
                    String responseBodyJson = responseBody.string();
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(responseBodyJson, JsonObject.class);

                    String error = jsonObject.getAsJsonObject("errors")
                            .getAsJsonArray("full_messages")
                            .get(0).getAsString();

                    onFail(error);
                } catch (Exception e) {
                    onFail(e.getMessage());
                }
            } else
                onFail(t.getMessage());
        }

        @Override
        public void onComplete() {

        }
    }
}
