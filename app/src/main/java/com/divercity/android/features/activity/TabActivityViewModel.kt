package com.divercity.android.features.activity

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.user.usecase.FetchUserDataUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabActivityViewModel @Inject
constructor(
    private val fetchUserDataUseCase: FetchUserDataUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    var fetchUserDataResponse = MutableLiveData<Resource<User>>()
    var adapterPosition: Int? = null

    fun fetchProfileData() {
        fetchUserDataResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<User>() {
            override fun onFail(error: String) {
                fetchUserDataResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchUserDataResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: User) {
                fetchUserDataResponse.postValue(Resource.success(o))
            }
        }
        fetchUserDataUseCase.execute(
            callback,
            FetchUserDataUseCase.Params.forUserData(sessionRepository.getUserId())
        )
    }

    override fun onCleared() {
        super.onCleared()
        fetchUserDataUseCase.dispose()
    }
}
