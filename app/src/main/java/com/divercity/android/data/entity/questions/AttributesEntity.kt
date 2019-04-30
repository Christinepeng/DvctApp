package com.divercity.android.data.entity.questions

import com.google.gson.annotations.SerializedName

data class AttributesEntity(

	@field:SerializedName("latest_answerers_info")
	val latestAnswerersInfo: List<LatestAnswerersInfoItemEntity?>? = null,

	@field:SerializedName("embedded_attachment_id")
	val embeddedAttachmentId: Int? = null,

	@field:SerializedName("question_type")
	val questionType: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("last_activity_info")
	val lastActivityInfo: LastActivityInfoEntity? = null,

	@field:SerializedName("answers_count")
	val answersCount: Int? = null,

	@field:SerializedName("has_embedded_attachment")
	val hasEmbeddedAttachment: Boolean? = null,

	@field:SerializedName("embedded_attachment_type")
	val embeddedAttachmentType: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("author_id")
	val authorId: Int? = null,

	@field:SerializedName("author_info")
	val authorInfo: AuthorInfoEntity? = null,

	@field:SerializedName("group")
	val group: List<GroupItemEntity?>? = null,

	@field:SerializedName("picture_main")
	val pictureMain: String? = null
)