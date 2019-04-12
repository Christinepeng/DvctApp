package com.divercity.android.features.chat.newgroupchat

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.repository.paginated.UsersByCharacterPaginatedRepository
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */
 
class NewGroupChatViewModel @Inject
constructor(repository : UsersByCharacterPaginatedRepository)
    : BaseViewModelPagination<Any>(repository){

    init {
        fetchData(null, "")
    }
}
