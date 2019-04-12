package com.divercity.android.features.chat.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchUsersByCharacterUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: UserRepository
) : UseCase<@JvmSuppressWildcards List<Any>, Params>(executorThread, uiThread) {

    private val firstChars = ArrayList<Char>()

    override fun createObservableUseCase(params: Params): Observable<List<Any>> {
        return repository.fetchUsers(
            params.page,
            params.size,
            params.query
        ).map {
            if(params.page == 0)
                firstChars.clear()
            return@map getSegmentedList(it)
        }
    }

    private fun getSegmentedList(data: List<UserResponse>): ArrayList<Any> {
        val result = ArrayList<Any>()

        for (r in data) {
            val firstChar = Character.toUpperCase(r.userAttributes!!.name!![0])
            if (!firstChars.contains(firstChar)) {
                firstChars.add(firstChar)
                result.add(firstChar)
            }
            result.add(r)
        }

        return result
    }
}
