package com.divercity.app.data.entity.chat.messages

import com.google.gson.annotations.SerializedName

data class SubscriptionsItem(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("date_deleted")
	val dateDeleted: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("chat_id")
	val chatId: Int? = null,

	@field:SerializedName("deletion_restored_through")
	val deletionRestoredThrough: String? = null,

	@field:SerializedName("deleted")
	val deleted: Boolean? = null,

	@field:SerializedName("blocked")
	val blocked: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("notification_muted")
	val notificationMuted: Boolean? = null,

	@field:SerializedName("last_message_created_date")
	val lastMessageCreatedDate: String? = null,

	@field:SerializedName("reported")
	val reported: Boolean? = null,

	@field:SerializedName("id")
	val id: Int? = null
)