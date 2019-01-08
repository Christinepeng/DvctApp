package com.divercity.app.features.gender.onboarding;

import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.data.entity.profile.profile.User;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class OnboardingGenderViewModel extends BaseViewModel {

    private UserRepository userRepository;
    private MutableLiveData<Resource<UserResponse>> updateUserProfileResponse = new MutableLiveData<>();
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public OnboardingGenderViewModel(UpdateUserProfileUseCase updateUserProfileUseCase,
                                     UserRepository userRepository) {
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.userRepository = userRepository;
    }

    public void updateUserProfile(String gender){
        updateUserProfileResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<UserResponse>() {
            @Override
            protected void onFail(String error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(UserResponse o) {
                updateUserProfileResponse.postValue(Resource.Companion.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        User user = new User();
        user.setGender(gender);
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public MutableLiveData<Resource<UserResponse>> getUpdateUserProfileResponse() {
        return updateUserProfileResponse;
    }
}
