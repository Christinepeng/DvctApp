package com.divercity.android.features.chat.chat.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchChatMembersUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: ChatRepository,
            private val userRepository: UserRepository
) : UseCase<@JvmSuppressWildcards List<UserResponse>, FetchChatMembersUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<UserResponse>> {
        return repository.fetchChatMembers(
                userRepository.getUserId()!!,
                params.chatId,
                params.page,
                params.size,
                if (params.query != null && params.query == "") null else params.query)
    }

    class Params private constructor(val chatId: String, val page: Int,
                                     val size: Int, val query: String?) {

        companion object {

            fun forMember(chatId: String, page: Int, size: Int, query: String?): Params {
                return Params(chatId, page, size, query)
            }
        }
    }
}
