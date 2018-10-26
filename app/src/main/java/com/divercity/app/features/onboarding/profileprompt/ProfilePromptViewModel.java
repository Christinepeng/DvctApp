package com.divercity.app.features.onboarding.profileprompt;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class ProfilePromptViewModel extends BaseViewModel {

    UserRepository userRepository;

    @Inject
    public ProfilePromptViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAccountType() {
        return userRepository.getAccountType();
    }
}
