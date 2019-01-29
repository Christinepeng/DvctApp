package com.divercity.android.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.db.dao.ChatMessageDao
import com.divercity.android.db.dao.RecentChatsDao
import com.divercity.android.db.dao.UserDao

/**
 * Created by lucas on 30/12/2018.
 */

@Database(
    entities = [
        ChatMessageResponse::class,
        ExistingUsersChatListItem::class,
        UserResponse::class],
    version = AppDatabase.VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "divercity-db"
    }

    abstract fun chatMessageDao(): ChatMessageDao

    abstract fun existingChatDao(): RecentChatsDao

    abstract fun userDao(): UserDao
}