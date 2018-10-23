package com.divercity.app.features.profile.selectbirthdate;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.profile.usecase.UpdateUserProfileUseCase;
import com.divercity.app.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectBirthdayViewModel extends BaseViewModel {

    UserRepository userRepository;
    MutableLiveData<Resource<LoginResponse>> dataUpdateUser = new MutableLiveData<>();
    UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectBirthdayViewModel(@NonNull Application application,
                                   UpdateUserProfileUseCase updateUserProfileUseCase,
                                   UserRepository userRepository) {
        super(application);
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.userRepository = userRepository;
    }

    public void updateUserProfile(String year, String month, String day){
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
        user.setBirthdate(day + "/" + month + "/" + year);
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return userRepository.getCurrentUserAccountType();
    }
}
