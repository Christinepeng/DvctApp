package com.divercity.android.features.chat.recentchats

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.usecase.FetchCurrentChatsUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class RecentChatsPaginatedRepository @Inject
internal constructor(private val fetchCurrentChatsUseCase: FetchCurrentChatsUseCase) :
    BaseDataSourceRepository<ExistingUsersChatListItem>() {

    override fun getUseCase(): UseCase<List<ExistingUsersChatListItem>, Params> = fetchCurrentChatsUseCase
}
