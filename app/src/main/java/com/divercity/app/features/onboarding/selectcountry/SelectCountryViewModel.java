package com.divercity.app.features.onboarding.selectcountry;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectCountryViewModel extends BaseViewModel {

    private UserRepository userRepository;

    @Inject
    public SelectCountryViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

}
