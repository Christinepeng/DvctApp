package com.divercity.android.features.onboarding.professionalInfo;

import androidx.lifecycle.MutableLiveData;

import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.profile.profile.UserProfileEntity;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.android.model.user.User;
import com.divercity.android.repository.session.SessionRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class ProfessionalInfoViewModel extends BaseViewModel {

    MutableLiveData<Resource<User>> dataUpdateUser = new MutableLiveData<>();
    UpdateUserProfileUseCase updateUserProfileUseCase;
    SessionRepository sessionRepository;

    @Inject
    public ProfessionalInfoViewModel(
            UpdateUserProfileUseCase updateUserProfileUseCase,
            SessionRepository sessionRepository
    ) {
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.sessionRepository = sessionRepository;
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
