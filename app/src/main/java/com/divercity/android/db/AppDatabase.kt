package com.divercity.android.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.db.chat.ChatMessageDao
import com.divercity.android.db.chat.ExistingChatDao

/**
 * Created by lucas on 30/12/2018.
 */

@Database(entities = [ChatMessageResponse::class, ExistingUsersChatListItem::class], version = AppDatabase.VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "divercity-db"
    }

    abstract fun chatMessageDao() : ChatMessageDao

    abstract fun existingChatDao() : ExistingChatDao
}