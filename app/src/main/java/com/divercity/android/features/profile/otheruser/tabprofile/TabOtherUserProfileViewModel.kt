package com.divercity.android.features.profile.otheruser.tabprofile

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.selectinterests.usecase.FollowInterestsUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabOtherUserProfileViewModel @Inject
constructor(
    private val fetchInterestsUseCase: FetchInterestsUseCase,
    private val followInterestsUseCase: FollowInterestsUseCase
) : BaseViewModel() {

    var fetchInterestsResponse = MutableLiveData<Resource<List<InterestsResponse>>>()
    val updateUserProfileResponse = SingleLiveEvent<Resource<UserResponse>>()

    fun fetchInterests(user : UserResponse?) {
        fetchInterestsResponse.postValue(Resource.loading(null))
        val callback = object : DisposableObserverWrapper<List<InterestsResponse>>() {
            override fun onFail(error: String) {
                fetchInterestsResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                fetchInterestsResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<InterestsResponse>) {
                val interests = user?.userAttributes?.interestIds
                interests?.let {

                    for(i in o){
                        if(interests.contains(i.id?.toInt())){
                            i.isSelected = true
                        }
                    }
                }
                fetchInterestsResponse.postValue(Resource.success(o))
            }
        }
        fetchInterestsUseCase.execute(callback, null)
    }

    override fun onCleared() {
        super.onCleared()
        fetchInterestsUseCase.dispose()
        followInterestsUseCase.dispose()
    }
}
