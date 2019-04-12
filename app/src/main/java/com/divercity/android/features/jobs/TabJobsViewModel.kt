package com.divercity.android.features.jobs

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.repository.session.SessionRepository
import com.divercity.android.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabJobsViewModel @Inject
constructor(val userRepository: UserRepositoryImpl,
            private val sessionRepository: SessionRepository,
            val application: Application) : BaseViewModel() {

    val showBtnAddJob = MutableLiveData<Int>()
    var adapterPosition: Int? = null

    init {

        if (sessionRepository.isLoggedUserJobSeeker())
            showBtnAddJob.value = View.GONE
        else
            showBtnAddJob.value = View.VISIBLE
    }

    fun getAccountType(): String? {
        return sessionRepository.getAccountType()
    }
}
