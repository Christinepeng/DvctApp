package com.divercity.android.data.entity.questions

import com.divercity.android.model.Question
import com.google.gson.annotations.SerializedName

data class QuestionEntityResponse(

    @field:SerializedName("attributes")
    val attributes: AttributesEntity? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) {

    fun toQuestion() = Question(
        id = id,
        authorProfilePicUrl = attributes?.authorInfo?.avatarThumb,
        authorName = attributes?.authorInfo?.name,
        createdAt = attributes?.createdAt,
        question = attributes?.text,
        groupTitle = attributes?.group?.get(0)?.title,
        questionPicUrl = attributes?.pictureMain,
        groupId = attributes?.group?.get(0)?.id,
        authorId = attributes?.authorId.toString(),
        lastAnswer = attributes?.lastActivityInfo?.answerInfo?.text,
        lastAnswerAuthorName = attributes?.lastActivityInfo?.authorInfo?.name,
        lastAnswerAuthorPicture = attributes?.lastActivityInfo?.authorInfo?.avatarMedium
    )
}