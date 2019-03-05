package com.divercity.android.features.onboarding.profileprompt

import android.app.Application
import com.divercity.android.R
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class ProfilePromptViewModel @Inject
constructor(
    val userRepository: UserRepository,
    val application: Application,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    val showRecruiterText = SingleLiveEvent<Boolean>()

    init {
        showRecruiterText.value =
            accountType == application.getString(R.string.recruiter_id) ||
                    accountType == application.getString(R.string.hiring_manager_id)
    }

    val accountType
        get() = sessionRepository.getAccountType()

    val userName
        get() = sessionRepository.getUserName()
}
