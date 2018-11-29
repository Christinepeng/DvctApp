package com.divercity.app.features.signup.usecase

import com.divercity.app.core.base.UseCase
import com.divercity.app.data.entity.login.response.LoginResponse
import com.divercity.app.data.entity.profile.picture.ProfilePictureBody
import com.divercity.app.repository.user.UserRepository
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
): UseCase<LoginResponse, UploadProfilePictureUseCase.Params>(executorThread, uiThread){

    override fun createObservableUseCase(params: Params): Observable<LoginResponse> {

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