package com.divercity.android.features.profile.settings.accountsettings

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class AccountSettingsViewModel @Inject
constructor(
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    fun getName() : String?{
        return sessionRepository.getUserName()
    }

    fun getEmail() : String?{
        return sessionRepository.getEmail()
    }
}
