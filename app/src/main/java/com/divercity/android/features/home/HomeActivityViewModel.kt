package com.divercity.android.features.home

import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.repository.user.UserRepositoryImpl
import javax.inject.Inject

/**
 * Created by lucas on 01/11/2018.
 */

class HomeActivityViewModel @Inject
constructor(var userRepositoryImpl: UserRepositoryImpl) : BaseViewModel() {

    fun getProfilePictureUrl(): String? = userRepositoryImpl.getAvatarThumbUrl()

}