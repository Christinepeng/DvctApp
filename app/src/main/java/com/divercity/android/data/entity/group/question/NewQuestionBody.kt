package com.divercity.android.data.entity.group.question

import com.google.gson.annotations.SerializedName

data class NewQuestionBody(

    @field:SerializedName("question")
    val question: QuestionEntity? = null
) {
    constructor(
        question: String,
        groupId: String,
        image: String?
    ) : this(
        QuestionEntity(
            text = question,
            groupOfInterestIds = listOf(groupId),
            image = image
        )
    )
}