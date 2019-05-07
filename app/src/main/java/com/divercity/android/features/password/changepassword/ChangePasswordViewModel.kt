package com.divercity.android.features.password.changepassword

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.password.changepassword.usecase.ChangePasswordUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class ChangePasswordViewModel @Inject
constructor(
    private val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel() {

    val changePasswordResponse = SingleLiveEvent<Resource<Unit>>()

    fun changePassword(oldPassword : String, newPassword : String, confirmation: String) {
        changePasswordResponse.value = Resource.loading(null)
        val callback = object : DisposableObserverWrapper<Unit>() {

            override fun onFail(error: String) {
                changePasswordResponse.value = Resource.error(error, null)
            }

            override fun onHttpException(error: JsonElement?) {
                changePasswordResponse.value = Resource.error(error.toString(), null)
            }

            override fun onSuccess(t: Unit) {
                changePasswordResponse.value = Resource.success(t)
            }
        }
        changePasswordUseCase.execute(
            callback,
            ChangePasswordUseCase.Params.to(oldPassword, newPassword, confirmation)
        )
    }

}
