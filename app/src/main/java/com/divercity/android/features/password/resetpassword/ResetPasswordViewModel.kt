package com.divercity.android.features.password.resetpassword

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.password.resetpassword.usecase.ResetPasswordUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 27/09/2018.
 */

class ResetPasswordViewModel @Inject
constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel() {

    val resetPasswordResponse = SingleLiveEvent<Resource<Unit>>()

    fun resetPassword(password: String, token: String) {
        resetPasswordResponse.value = Resource.loading(null)
        val callback = object : DisposableObserverWrapper<Unit>() {

            override fun onFail(error: String) {
                resetPasswordResponse.value = Resource.error(error, null)
            }

            override fun onHttpException(error: JsonElement?) {
                resetPasswordResponse.value = Resource.error(error.toString(), null)
            }

            override fun onSuccess(t: Unit) {
                resetPasswordResponse.value = Resource.success(t)
            }
        }
        resetPasswordUseCase.execute(
            callback,
            ResetPasswordUseCase.Params.to(password, token)
        )
    }

}
