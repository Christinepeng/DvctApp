package com.divercity.android.db.dao

import androidx.room.*
import com.divercity.android.model.Education

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface EducationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEducations(educations : List<Education>)

    @Query("SELECT * FROM user")
    fun deleteEducations()

    @Transaction
    fun deleteAndInsertEducations(educations : List<Education>){
        deleteEducations()
        insertEducations(educations)
    }
}