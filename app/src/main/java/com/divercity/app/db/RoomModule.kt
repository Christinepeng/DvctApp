package com.divercity.app.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by lucas on 30/12/2018.
 */

@Module
object RoomModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideDatabase(context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatMessageDao(db: AppDatabase) : ChatMessageDao {
        return db.chatMessageDao()
    }
}