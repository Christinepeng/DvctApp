package com.divercity.app.data.entity.chat.currentchats

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

data class ExistingUsersChatListItem(

	@field:SerializedName("last_message_date")
	val lastMessageDate: String? = null,

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("chat_picture")
	val chatPicture: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("blocked_by_me")
	val blockedByMe: Boolean? = null,

	@field:SerializedName("last_message")
	val lastMessage: String? = null,

	@field:SerializedName("unread_message_count")
	val unreadMessageCount: Int? = null,

	@field:SerializedName("chat_id")
	val chatId: Int? = null,

	@field:SerializedName("chat_type")
	val chatType: String? = null,

	@field:SerializedName("blocked")
	val blocked: Boolean? = null,

	@field:SerializedName("notification_muted")
	val notificationMuted: Boolean? = null,

	@field:SerializedName("nickname")
	val nickname: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("chat_users")
	val chatUsers: List<ChatUsersItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("last_message_picture")
	val lastMessagePicture: String? = null,

	@field:SerializedName("chat_name")
	val chatName: String? = null
)