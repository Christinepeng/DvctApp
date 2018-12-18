package com.divercity.app.features.location.onboarding

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 09/11/2018.
 */

class OnboardingLocationViewModel @Inject
constructor(
        private val userRepository: UserRepository) : BaseViewModel() {

    fun getAccountType(): String {
        return userRepository.getAccountType()!!
    }
}