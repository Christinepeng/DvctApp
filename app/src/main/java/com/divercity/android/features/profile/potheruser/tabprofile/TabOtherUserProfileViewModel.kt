package com.divercity.android.features.profile.potheruser.tabprofile

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.core.utils.SingleLiveEvent
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.interests.InterestsResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.onboarding.selectinterests.usecase.FetchInterestsUseCase
import com.divercity.android.features.onboarding.selectinterests.usecase.FollowInterestsUseCase
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

    override fun onCleared() {
        super.onCleared()
        fetchInterestsUseCase.dispose()
        followInterestsUseCase.dispose()
    }
}
