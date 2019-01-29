package com.divercity.android.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.divercity.android.data.entity.user.response.UserResponse

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserResponse)

    @Query("SELECT * FROM user")
    fun getUser(): UserResponse

    @Query("DELETE FROM user")
    fun deleteUser()
}