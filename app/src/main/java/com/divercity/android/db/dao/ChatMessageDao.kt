package com.divercity.android.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatMessage(users: ChatMessageResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertChatMessages(list : List<ChatMessageResponse>)

    @Query("SELECT * FROM chatMessage WHERE chatId = :chatId ORDER BY id DESC")
    fun getPagedMessagesByChatId(chatId : Int): DataSource.Factory<Int, ChatMessageResponse>

    @Query("SELECT COUNT(*) FROM chatMessage WHERE chatId = :chatId ")
    fun countMessagesByChatId(chatId : Int): Int

    @Query("DELETE FROM chatMessage")
    suspend fun deleteChatMessages()
}