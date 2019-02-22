package com.divercity.android.db.dao

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
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