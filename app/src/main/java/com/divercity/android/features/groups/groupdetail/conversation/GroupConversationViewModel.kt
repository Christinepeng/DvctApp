package com.divercity.android.features.groups.groupdetail.conversation

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.data.entity.questions.QuestionResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class GroupConversationViewModel @Inject
constructor(
    repository: GroupConversationPaginatedRepository
) : BaseViewModelPagination<QuestionResponse>(repository) {

    private var groupId: String? = null

    fun fetchConversations(groupId : String) {
        if(this.groupId == null) {
            this.groupId = groupId
            (repository as GroupConversationPaginatedRepository).setGroupId(groupId)
            fetchData()
        }
    }
}