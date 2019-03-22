package com.divercity.android.features.groups.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class JoinGroupUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val userRepository: UserRepository
) : UseCase<Boolean, JoinGroupUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        return userRepository.joinGroup(params.groupId)
    }

    class Params private constructor(val groupId: String) {

        companion object {

            fun forJoin(groupId: String) : Params {
                return Params(groupId)
            }
        }
    }
}
