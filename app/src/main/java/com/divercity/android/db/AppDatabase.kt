package com.divercity.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.user.response.UserEntityResponse
import com.divercity.android.db.dao.ChatMessageDao
import com.divercity.android.db.dao.GroupDao
import com.divercity.android.db.dao.RecentChatsDao
import com.divercity.android.db.dao.UserDao

/**
 * Created by lucas on 30/12/2018.
 */

@Database(
    entities = [
        ChatMessageEntityResponse::class,
        ExistingUsersChatListItem::class,
        UserEntityResponse::class,
        AnswerResponse::class],
    version = AppDatabase.VERSION,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 3
        const val DATABASE_NAME = "divercity-db"
    }

    abstract fun chatMessageDao(): ChatMessageDao

    abstract fun existingChatDao(): RecentChatsDao

    abstract fun userDao(): UserDao

    abstract fun groupDao(): GroupDao
}