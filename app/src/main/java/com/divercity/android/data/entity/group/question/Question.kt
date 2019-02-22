package com.divercity.android.data.entity.group.question

import com.google.gson.annotations.SerializedName

data class Question(

    @field:SerializedName("question_type")
    val questionType: String = "topic",

    @field:SerializedName("group_of_interest_ids")
    val groupOfInterestIds: List<String?>? = null,

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("picture")
    val image: String? = null
)