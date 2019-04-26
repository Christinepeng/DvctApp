package com.divercity.android.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divercity.android.data.entity.user.response.UserEntityResponse

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntityResponse)

    @Query("SELECT * FROM user")
    fun getUser(): LiveData<UserEntityResponse>

    @Query("SELECT * FROM user")
    fun getUserSync(): UserEntityResponse

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}