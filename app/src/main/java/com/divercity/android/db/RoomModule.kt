package com.divercity.android.db

import android.arch.persistence.room.Room
import android.content.Context
import com.divercity.android.db.dao.ChatMessageDao
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

//    private val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE user ADD COLUMN attr_skills TEXT")
//        }
//    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideDatabase(context: Context) : AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
//            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideChatMessageDao(db: AppDatabase) : ChatMessageDao {
        return db.chatMessageDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideExistingChatDao(db: AppDatabase) : RecentChatsDao {
        return db.existingChatDao()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideUserDato(db: AppDatabase) : UserDao {
        return db.userDao()
    }
}