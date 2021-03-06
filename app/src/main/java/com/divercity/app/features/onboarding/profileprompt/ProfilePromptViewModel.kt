package com.divercity.app.features.onboarding.profileprompt

import android.app.Application
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class ProfilePromptViewModel @Inject
constructor(val userRepository: UserRepository,
            val application: Application) : BaseViewModel() {

    val showRecruiterText = SingleLiveEvent<Boolean>()

    init {
        showRecruiterText.value =
                accountType == application.getString(R.string.recruiter_id) ||
                accountType == application.getString(R.string.hiring_manager_id)
    }

    val accountType: String?
        get() = userRepository.getAccountType()
}
