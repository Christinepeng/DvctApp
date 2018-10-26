package com.divercity.app.features.signup;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.signup.response.SignUpResponse;
import com.divercity.app.features.signup.usecase.SignUpUseCase;

import javax.inject.Inject;

/**
 * Created by lucas on 27/09/2018.
 */

public class SignUpViewModel extends BaseViewModel {

    private SignUpUseCase signUpUseCase;

    private SingleLiveEvent<Resource<Void>> signUp = new SingleLiveEvent<>();

    @Inject
    public SignUpViewModel(SignUpUseCase signUpUseCase) {
        this.signUpUseCase = signUpUseCase;
    }

    public void signUp(String name, String username, String email, String password, String confirmPassword) {
        signUp.setValue(Resource.loading(null));
        SignUpUseCase.Callback callback = new SignUpUseCase.Callback() {
            @Override
            protected void onFail(String msg) {
                signUp.setValue(Resource.error(msg, null));
            }

            @Override
            protected void onSuccess(SignUpResponse response) {
                signUp.setValue(Resource.success(null));
            }
        };
        getCompositeDisposable().add(callback);
        signUpUseCase.execute(callback, SignUpUseCase.Params.forSignUp(
                username,
                name,
                email,
                password,
                confirmPassword));
    }

    public SingleLiveEvent<Resource<Void>> getSignUp() {
        return signUp;
    }

}
