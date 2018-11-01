package com.divercity.app.features.home

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class HomeActivityViewModel @Inject
constructor(var userRepositoryImpl: UserRepositoryImpl) : BaseViewModel() {

    fun getProfilePictureUrl(): String? = userRepositoryImpl.getAvatarUrl()

}