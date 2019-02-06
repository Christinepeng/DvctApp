package com.divercity.android.db.dao

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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
    fun insertChatMessages(list: List<ExistingUsersChatListItem>)

    @Query("SELECT * FROM recentChat ORDER BY lastMessageDate DESC")
    fun getPagedRecentChats(): DataSource.Factory<Int, ExistingUsersChatListItem>

    @Query("SELECT * FROM recentChat ORDER BY lastMessageDate DESC")
    fun getRecentChats(): LiveData<List<ExistingUsersChatListItem>>

    @Query("SELECT chatId FROM recentChat WHERE chatUsers LIKE '%'||:userId||'%'  AND chatType != 'group'")
    fun fetchChatIdByUser(userId: String): Int

    @Query("DELETE FROM recentChat")
    fun deleteRecentChats()
}