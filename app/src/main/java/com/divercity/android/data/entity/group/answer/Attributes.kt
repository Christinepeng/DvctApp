package com.divercity.android.data.entity.group.answer

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Attributes(

	@field:SerializedName("raw_text")
	val rawText: String? = null,

	@field:SerializedName("aggregated_sentiment_counts")
	val aggregatedSentimentCounts: List<String?>? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("report_count")
	val reportCount: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("voted")
	val voted: Int? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null,

	@field:SerializedName("current_user_sentiment_id")
	val currentUserSentimentId: Int? = null,

	@field:SerializedName("location_display_name")
	val locationDisplayName: String? = null,

	@field:SerializedName("votesdown")
	val votesdown: Int? = null,

	@field:SerializedName("sentiment_type")
	val sentimentType: String? = null,

	@field:SerializedName("replied_to_id")
	val repliedToId: Int? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("author_id")
	val authorId: Int? = null,

	@field:SerializedName("is_flagged")
	val isFlagged: Boolean? = null,

	@field:SerializedName("votesup")
	val votesup: Int? = null,

	@field:SerializedName("author_info")
	val authorInfo: AuthorInfo? = null
)