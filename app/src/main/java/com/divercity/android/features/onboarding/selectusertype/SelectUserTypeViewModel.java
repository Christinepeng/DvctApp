package com.divercity.android.features.onboarding.selectusertype;

import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.profile.profile.UserProfileEntity;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.android.model.user.User;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectUserTypeViewModel extends BaseViewModel {

    MutableLiveData<Resource<User>> dataUpdateUser = new MutableLiveData<>();
    UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectUserTypeViewModel(UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    public void updateUserProfile(String typeId){
        dataUpdateUser.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<User>() {
            @Override
            protected void onFail(String error) {
                dataUpdateUser.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                dataUpdateUser.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(User o) {
                dataUpdateUser.postValue(Resource.Companion.success(o));
            }
        };
        UserProfileEntity user = new UserProfileEntity();
        user.setAccountType(typeId);
        updateUserProfileUseCase.execute(callback,new UpdateUserProfileUseCase.Params(user));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        updateUserProfileUseCase.dispose();
    }
}
