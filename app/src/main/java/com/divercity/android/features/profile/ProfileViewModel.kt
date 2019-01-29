package com.divercity.android.features.profile

import android.arch.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.usecase.FetchUserDataUseCase
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class ProfileViewModel @Inject
constructor(private val fetchUserDataUseCase: FetchUserDataUseCase,
            private val sessionRepository: SessionRepository) : BaseViewModel() {

    var fetchUserDataResponse = MutableLiveData<Resource<UserResponse>>()

    init {
        fetchProfileData()
    }

    fun fetchProfileData() {
        fetchUserDataResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                fetchUserDataResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUserDataResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: UserResponse) {
                fetchUserDataResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchUserDataUseCase.execute(callback, FetchUserDataUseCase.Params.forUserData(sessionRepository.getUserId()))
    }
}
