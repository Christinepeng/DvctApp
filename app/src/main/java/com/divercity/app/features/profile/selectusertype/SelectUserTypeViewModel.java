package com.divercity.app.features.profile.selectusertype;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.profile.usecase.UpdateUserProfileUseCase;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeViewModel extends BaseViewModel {

    MutableLiveData<Resource<LoginResponse>> dataUpdateUser = new MutableLiveData<>();
    UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectUserTypeViewModel(@NonNull Application application,
                                   UpdateUserProfileUseCase updateUserProfileUseCase) {
        super(application);
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    public void updateUserProfile(String typeId){
        dataUpdateUser.postValue(Resource.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<LoginResponse>() {
            @Override
            protected void onFail(String error) {
                dataUpdateUser.postValue(Resource.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                dataUpdateUser.postValue(Resource.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(LoginResponse o) {
                dataUpdateUser.postValue(Resource.success(o));
            }
        };
        getCompositeDisposable().add(callback);
        User user = new User();
        user.setAccountType(typeId);
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }
}
