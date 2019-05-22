package com.divercity.android.data.entity.group.question

import com.google.gson.annotations.SerializedName

data class NewQuestionBody(

    @field:SerializedName("question")
    val question: QuestionEntity? = null
) {
    constructor(
        question: String,
        groupIds: List<String>,
        image: String?
    ) : this(
        QuestionEntity(
            text = question,
            groupOfInterestIds = groupIds,
            image = image
        )
    )
}