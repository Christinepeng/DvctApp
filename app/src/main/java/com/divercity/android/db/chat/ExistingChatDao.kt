package com.divercity.android.db.chat

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface ExistingChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ExistingUsersChatListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertChatMessages(list : List<ExistingUsersChatListItem>)

    @Query("SELECT chatId FROM existingChat WHERE chatUsers LIKE '%'||:userId||'%'  AND chatType != 'group'")
    fun fetchChatIdByUser(userId : String): Int
}