package com.divercity.app.features.profile.selectethnicity;

import android.app.Application;
import android.support.annotation.NonNull;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectEthnicityViewModel extends BaseViewModel {

    UserRepository userRepository;

    @Inject
    public SelectEthnicityViewModel(@NonNull Application application,
                                    UserRepository userRepository) {
        super(application);
        this.userRepository = userRepository;
    }

    public String getAccountType(){
        return userRepository.getCurrentUserAccountType();
    }
}
