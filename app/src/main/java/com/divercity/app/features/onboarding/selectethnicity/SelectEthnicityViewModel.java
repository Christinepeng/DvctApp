package com.divercity.app.features.onboarding.selectethnicity;

import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectEthnicityViewModel extends BaseViewModel {

    private UserRepository userRepository;
    private MutableLiveData<Resource<LoginResponse>> updateUserProfileResponse = new MutableLiveData<>();
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectEthnicityViewModel(UserRepository userRepository,
                                    UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.userRepository = userRepository;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    public void updateUserProfile(String ethnicity){
        updateUserProfileResponse.postValue(Resource.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<LoginResponse>() {
            @Override
            protected void onFail(String error) {
                updateUserProfileResponse.postValue(Resource.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                updateUserProfileResponse.postValue(Resource.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(LoginResponse o) {
                updateUserProfileResponse.postValue(Resource.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        User user = new User();
        user.setEthnicity(ethnicity);
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public MutableLiveData<Resource<LoginResponse>> getUpdateUserProfileResponse() {
        return updateUserProfileResponse;
    }
}
