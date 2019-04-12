package com.divercity.android.features.chat.recentchats.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.repository.chat.ChatRepository
import com.divercity.android.repository.session.SessionRepository
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
            private val sessionRepository: SessionRepository
) : UseCase<@JvmSuppressWildcards List<ExistingUsersChatListItem>, Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<List<ExistingUsersChatListItem>> {
        return repository.fetchCurrentChats(
                sessionRepository.getUserId(),
                params.page,
                params.size,
                params.query)
    }

}
