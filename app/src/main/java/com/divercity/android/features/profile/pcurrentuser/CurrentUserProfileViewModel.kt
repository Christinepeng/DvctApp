package com.divercity.android.features.profile.pcurrentuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.profile.usecase.FetchLoggedUserDataUseCase
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class CurrentUserProfileViewModel @Inject
constructor(
    private val fetchLoggedUserDataUseCase: FetchLoggedUserDataUseCase,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    // CurrentUserProfileFragment
    var fetchUserDataResponse = MutableLiveData<Resource<User>>()

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
        fetchLoggedUserDataUseCase.execute(
            callback,
            null
        )
    }

    fun getCurrentUser(): LiveData<User> {
        return sessionRepository.getUserDB()
    }

    fun getCurrentUserId(): String {
        return sessionRepository.getUserId()
    }

    override fun onCleared() {
        super.onCleared()
        fetchLoggedUserDataUseCase.dispose()
    }
}
