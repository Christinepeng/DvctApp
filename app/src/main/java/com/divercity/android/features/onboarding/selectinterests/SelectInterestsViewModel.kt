package com.divercity.android.features.onboarding.selectinterests

import android.arch.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.selectinterests.usecase.FollowInterestsUseCase
import com.divercity.android.repository.user.UserRepository
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class SelectInterestsViewModel @Inject
constructor(private val fetchInterestsUseCase: FetchInterestsUseCase,
            private val followInterestsUseCase: FollowInterestsUseCase,
            val userRepository: UserRepository) : BaseViewModel() {

    var fetchInterestsResponse = MutableLiveData<Resource<List<InterestsResponse>>>()
    var followInterestsResponse = MutableLiveData<Resource<UserResponse>>()

    init {
        fetchInterests()
    }

    fun fetchInterests() {
        fetchInterestsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<InterestsResponse>>() {
            override fun onFail(error: String) {
                fetchInterestsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchInterestsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<InterestsResponse>) {
                fetchInterestsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        fetchInterestsUseCase.execute(callback, null)
    }

    fun followInterests(interestsIds: List<String>) {
        followInterestsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<UserResponse>() {
            override fun onFail(error: String) {
                followInterestsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                followInterestsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: UserResponse) {
                followInterestsResponse.postValue(Resource.success(o))
            }
        }
        compositeDisposable.add(callback)
        followInterestsUseCase.execute(callback, FollowInterestsUseCase.Params.forInterests(interestsIds))
    }

    fun getAccountType(): String {
        return userRepository.getAccountType()!!
    }
}