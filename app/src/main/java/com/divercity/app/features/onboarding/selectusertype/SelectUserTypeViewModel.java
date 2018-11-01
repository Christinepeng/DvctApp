package com.divercity.app.features.onboarding.selectusertype;

import android.arch.lifecycle.MutableLiveData;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.profile.User;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeViewModel extends BaseViewModel {

    MutableLiveData<Resource<LoginResponse>> dataUpdateUser = new MutableLiveData<>();
    UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectUserTypeViewModel(UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    public void updateUserProfile(String typeId){
        dataUpdateUser.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<LoginResponse>() {
            @Override
            protected void onFail(String error) {
                dataUpdateUser.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                dataUpdateUser.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(LoginResponse o) {
                dataUpdateUser.postValue(Resource.Companion.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        User user = new User();
        user.setAccountType(typeId);
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }
}
