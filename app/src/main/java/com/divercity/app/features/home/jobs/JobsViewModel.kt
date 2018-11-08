package com.divercity.app.features.home.jobs

import android.app.Application
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.core.utils.SingleLiveEvent
import com.divercity.app.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobsViewModel @Inject
constructor(val userRepository: UserRepositoryImpl,
            val application: Application) : BaseViewModel() {

    val showBtnAddJob = SingleLiveEvent<Int>()

    init {

        if (userRepository.getAccountType() != null &&
                (userRepository.getAccountType().equals(application.getString(R.string.hiring_manager_id)) ||
                        userRepository.getAccountType().equals(application.getString(R.string.recruiter_id)))
        )
            showBtnAddJob.value = View.VISIBLE
        else
            showBtnAddJob.value = View.GONE
    }

    fun getAccountType(): String? {
        return userRepository.getAccountType()
    }
}
