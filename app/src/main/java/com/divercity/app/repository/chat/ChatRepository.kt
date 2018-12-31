package com.divercity.app.repository.chat

import com.divercity.app.data.entity.createchat.CreateChatResponse
import io.reactivex.Observable

/**
 * Created by lucas on 29/10/2018.
 */

interface ChatRepository {

    fun createChat(currentUserId : String, otherUserId: String): Observable<CreateChatResponse>
}