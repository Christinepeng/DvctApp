package com.divercity.android.data.entity.group.answer.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.TypeConverters
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
data class AnswerEntityResponse(

    @field:SerializedName("attributes")
    @Embedded(prefix = "attr_")
    var attributes: AttributesEntity? = null,

    @field:SerializedName("id")
    var id: String = "-1",

    @field:SerializedName("type")
    var type: String? = null
) {

    @Ignore
    @field:SerializedName("relationships")
    var relationships: RelationshipsEntity? = null

}