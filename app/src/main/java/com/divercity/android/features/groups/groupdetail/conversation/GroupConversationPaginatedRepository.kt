package com.divercity.android.features.groups.groupdetail.conversation

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.features.groups.groupdetail.conversation.usecase.FetchGroupConversationsCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class GroupConversationPaginatedRepository @Inject
constructor(private val fetchGroupConversationsCase: FetchGroupConversationsCase) :
    BaseDataSourceRepository<QuestionResponse>() {

    override fun getUseCase(): UseCase<List<QuestionResponse>, Params> {
        return fetchGroupConversationsCase
    }

    fun setGroupId(groupId: String) {
        fetchGroupConversationsCase.groupId = groupId
    }
}
