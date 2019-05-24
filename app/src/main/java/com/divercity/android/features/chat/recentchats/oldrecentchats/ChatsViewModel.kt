package com.divercity.android.features.chat.recentchats.oldrecentchats

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.features.chat.recentchats.RecentChatsPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class ChatsViewModel @Inject
constructor(repository: RecentChatsPaginatedRepository)
    : BaseViewModelPagination<ExistingUsersChatListItem>(repository){

    init {
        fetchData()
    }
}
