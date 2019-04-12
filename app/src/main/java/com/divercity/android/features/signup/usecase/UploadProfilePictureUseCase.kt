package com.divercity.android.features.signup.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.profile.picture.ProfilePictureBody
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 31/10/2018.
 */
 
class UploadProfilePictureUseCase @Inject
constructor(@Named("executor_thread") executorThread : Scheduler,
            @Named("ui_thread") uiThread : Scheduler,
            val userRepository: UserRepository
): UseCase<UserResponse, UploadProfilePictureUseCase.Params>(executorThread, uiThread){

    override fun createObservableUseCase(params: Params): Observable<UserResponse> {

        return userRepository.uploadUserPhoto(ProfilePictureBody(params.strFileBase64))
    }

    class Params private constructor(val strFileBase64: String) {

        companion object {

            fun forUploadPic(strFileBase64: String): Params {
                return Params(strFileBase64)
            }
        }
    }
}