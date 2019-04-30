package com.divercity.android.data.entity.group.answer.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class AttributesEntity(

	@field:SerializedName("raw_text")
	var rawText: String? = null,

	@field:SerializedName("aggregated_sentiment_counts")
	var aggregatedSentimentCounts: List<String>? = Collections.emptyList(),

	@field:SerializedName("images")
	var images: List<String>? = null,

	@field:SerializedName("report_count")
	var reportCount: Int? = null,

	@field:SerializedName("created_at")
	var createdAt: Date? = null,

	@field:SerializedName("voted")
	var voted: String? = null,

	@field:SerializedName("question_id")
	var questionId: Int? = null,

	@field:SerializedName("current_user_sentiment_id")
	var currentUserSentimentId: Int? = null,

	@field:SerializedName("location_display_name")
	var locationDisplayName: String? = null,

	@field:SerializedName("votesdown")
	var votesdown: Int? = null,

	@field:SerializedName("sentiment_type")
	var sentimentType: String? = null,

	@field:SerializedName("replied_to_id")
	var repliedToId: Int? = null,

	@field:SerializedName("text")
	var text: String? = null,

	@field:SerializedName("author_id")
	var authorId: Int? = null,

	@field:SerializedName("is_flagged")
	var isFlagged: Boolean? = null,

	@field:SerializedName("votesup")
	var votesup: Int? = null,

	@field:SerializedName("author_info")
	var authorInfo: AuthorInfoEntity? = null
)