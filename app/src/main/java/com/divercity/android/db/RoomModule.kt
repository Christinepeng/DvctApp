package com.divercity.android.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.divercity.android.db.dao.ChatMessageDao
import com.divercity.android.db.dao.GroupDao
import com.divercity.android.db.dao.RecentChatsDao
import com.divercity.android.db.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by lucas on 30/12/2018.
 */

@Module
object RoomModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `answers` (`id` TEXT NOT NULL, `type` TEXT, `attr_rawText` TEXT, `attr_aggregatedSentimentCounts` TEXT, `attr_images` TEXT, `attr_reportCount` INTEGER, `attr_createdAt` INTEGER, `attr_voted` TEXT, `attr_questionId` INTEGER, `attr_currentUserSentimentId` INTEGER, `attr_locationDisplayName` TEXT, `attr_votesdown` INTEGER, `attr_sentimentType` TEXT, `attr_repliedToId` INTEGER, `attr_text` TEXT, `attr_authorId` INTEGER, `attr_isFlagged` INTEGER, `attr_votesup` INTEGER, `attr_authorInfo` TEXT, PRIMARY KEY(`id`))")
        }
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatMessageDao(db: AppDatabase): ChatMessageDao {
        return db.chatMessageDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideExistingChatDao(db: AppDatabase): RecentChatsDao {
        return db.existingChatDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideGroupDao(db: AppDatabase): GroupDao {
        return db.groupDao()
    }
}