package com.divercity.android.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divercity.android.data.entity.group.answer.response.AnswerResponse

/**
 * Created by lucas on 30/12/2018.
 */

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAnswer(answer : AnswerResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAnswers(list : List<AnswerResponse>)

    @Query("SELECT * FROM answers WHERE attr_questionId = :questionId ORDER BY id DESC")
    fun getPagedAnswersByQuestionId(questionId : Int): DataSource.Factory<Int, AnswerResponse>
}