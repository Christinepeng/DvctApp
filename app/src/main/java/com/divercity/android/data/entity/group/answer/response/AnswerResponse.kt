package com.divercity.android.data.entity.group.answer.response

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.TypeConverters
import com.divercity.android.db.converter.AuthorInfoConverter
import com.divercity.android.db.converter.DateTypeConverter
import com.divercity.android.db.converter.IntListTypeConverter
import com.divercity.android.db.converter.StringListTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "answers", primaryKeys = ["id"])
@TypeConverters(
    AuthorInfoConverter::class,
    IntListTypeConverter::class,
    StringListTypeConverter::class,
    DateTypeConverter::class
)
data class AnswerResponse(

    @field:SerializedName("attributes")
    @Embedded(prefix = "attr_")
    var attributes: Attributes? = null,

    @field:SerializedName("id")
    var id: String = "-1",

    @field:SerializedName("type")
    var type: String? = null
) {

    @Ignore
    @field:SerializedName("relationships")
    var relationships: Relationships? = null

}