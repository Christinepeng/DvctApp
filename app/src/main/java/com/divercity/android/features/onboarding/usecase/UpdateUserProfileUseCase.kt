package com.divercity.android.features.onboarding.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.profile.profile.UserProfileEntity
import com.divercity.android.model.user.User
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UpdateUserProfileUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userRepository: UserRepository
) : UseCase<User, UpdateUserProfileUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<User> {
        return userRepository.updateLoggedUserProfile(params.user)
    }

    class Params constructor(val user: UserProfileEntity)
}
