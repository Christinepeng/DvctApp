package com.divercity.android.features.chat.recentchats.usecase

import com.divercity.android.core.base.UseCase
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchCurrentChatsUseCase @Inject
constructor(@Named("executor_thread") executorThread: Scheduler,
            @Named("ui_thread") uiThread: Scheduler,
            private val repository: ChatRepository,
            private val userRepository: UserRepository
) : UseCase<@JvmSuppressWildcards List<ExistingUsersChatListItem>, FetchCurrentChatsUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<ExistingUsersChatListItem>> {
        return repository.fetchCurrentChats(
                userRepository.getUserId()!!,
                params.page,
                params.size,
                if (params.query != null && params.query == "") null else params.query)
    }

    class Params private constructor(val page: Int, val size: Int, val query: String?) {

        companion object {

            fun forChat(page: Int, size: Int, query: String?): Params {
                return Params(page, size, query)
            }
        }
    }
}
