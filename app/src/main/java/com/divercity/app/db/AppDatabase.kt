package com.divercity.app.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.db.chat.Chat
import com.divercity.app.db.chat.ChatMessageDao

/**
 * Created by lucas on 30/12/2018.
 */

@Database(entities = [ChatMessageResponse::class, Chat::class], version = AppDatabase.VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "divercity-db"
    }

    abstract fun chatMessageDao() : ChatMessageDao
}