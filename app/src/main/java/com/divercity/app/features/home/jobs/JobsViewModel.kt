package com.divercity.app.features.home.jobs

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class JobsViewModel @Inject
constructor(val userRepositoryImpl: UserRepositoryImpl) : BaseViewModel(){

    fun getAccountType(): String? {
        return userRepositoryImpl.getAccountType()
    }
}
