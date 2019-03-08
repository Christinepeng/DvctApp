package com.divercity.android.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface RecentChatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ExistingUsersChatListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertChatMessages(list: List<ExistingUsersChatListItem>)

    @Query("SELECT * FROM recentChat ORDER BY lastMessageDate DESC")
    fun getPagedRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem>

    @Query("SELECT * FROM recentChat ORDER BY lastMessageDate DESC")
    fun getRecentChats(): LiveData<List<ExistingUsersChatListItem>>

    @Query("SELECT chatId FROM recentChat WHERE chatUsers LIKE '%'||:userId||'%'  AND chatType != 'group'")
    fun fetchChatIdByUser(userId: String): Int

    @Query("DELETE FROM recentChat")
    suspend fun deleteRecentChats()
}