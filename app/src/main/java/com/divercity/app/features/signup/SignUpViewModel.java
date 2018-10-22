package com.divercity.app.features.signup;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

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

    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> username = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> confirmPassword = new ObservableField<>();

    private SingleLiveEvent<Resource<Void>> signUp = new SingleLiveEvent<>();

    @Inject
    public SignUpViewModel(@NonNull Application application, SignUpUseCase signUpUseCase) {
        super(application);
        this.signUpUseCase = signUpUseCase;
    }

    public void signUp() {
        signUp.setValue(Resource.loading(null));
        SignUpUseCase.SignUpCaseCallback callback = new SignUpUseCase.SignUpCaseCallback() {
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
                username.get(),
                name.get(),
                email.get(),
                password.get(),
                confirmPassword.get()));
    }

    public SingleLiveEvent<Resource<Void>> getSignUp() {
        return signUp;
    }

}
