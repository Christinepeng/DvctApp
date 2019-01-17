package com.divercity.app.data.entity.chat.currentchats

import android.arch.persistence.room.Entity
import com.divercity.app.data.entity.createchat.UsersItem
import com.google.gson.annotations.SerializedName

@Entity(tableName = "existingChat",primaryKeys = ["chatId"])
data class ExistingUsersChatListItem(

    @field:SerializedName("last_message_date")
    var lastMessageDate: String? = "",

    @field:SerializedName("avatar_medium")
    var avatarMedium: String? = "",

    @field:SerializedName("chat_picture")
    var chatPicture: String? = "",

    @field:SerializedName("avatar_thumb")
    var avatarThumb: String? = "",

    @field:SerializedName("blocked_by_me")
    var blockedByMe: Boolean? = false,

    @field:SerializedName("last_message")
    var lastMessage: String? = "",

    @field:SerializedName("unread_message_count")
    var unreadMessageCount: Int? = -1,

    @field:SerializedName("chat_id")
    var chatId: Int = -1,

    @field:SerializedName("chat_type")
    var chatType: String? = "",

    @field:SerializedName("blocked")
    var blocked: Boolean? = false,

    @field:SerializedName("notification_muted")
    var notificationMuted: Boolean? = false,

    @field:SerializedName("nickname")
    var nickname: String? = "",

    @field:SerializedName("name")
    var name: String? = "",

    @field:SerializedName("chat_users")
    var chatUsers: List<UsersItem>? = ArrayList(),

    @field:SerializedName("id")
    var id: Int? = -1,

    @field:SerializedName("email")
    var email: String? = "",

    @field:SerializedName("last_message_picture")
    var lastMessagePicture: String? = "",

    @field:SerializedName("chat_name")
    var chatName: String? = ""
)
//{
//    constructor() : this(
//        0, "", "", "", "",
//        false, "", 0, 0, "",
//        false, false, "", "", emptyList(),
//        0, "", "", ""
//    )
//}