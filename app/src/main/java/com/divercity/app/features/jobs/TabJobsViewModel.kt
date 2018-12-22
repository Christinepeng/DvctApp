package com.divercity.app.features.jobs

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabJobsViewModel @Inject
constructor(val userRepository: UserRepositoryImpl,
            val application: Application) : BaseViewModel() {

    val showBtnAddJob = MutableLiveData<Int>()

    init {

        if (userRepository.isLoggedUserJobSeeker())
            showBtnAddJob.value = View.GONE
        else
            showBtnAddJob.value = View.VISIBLE
    }

    fun getAccountType(): String? {
        return userRepository.getAccountType()
    }
}
