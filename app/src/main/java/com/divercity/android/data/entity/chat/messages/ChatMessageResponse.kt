package com.divercity.android.data.entity.chat.messages

import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chatMessage", primaryKeys = ["id", "chatId"])
data class ChatMessageResponse(

    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("message_created_at")
    var messageCreatedAt: String? = null,

    @field:SerializedName("embedded_attachment_id")
    var embeddedAttachmentId: String? = null,

    @field:SerializedName("from_username")
    var fromUsername: String? = null,

    @field:SerializedName("has_embedded_attachment")
    var hasEmbeddedAttachment: Boolean? = null,

    @field:SerializedName("embedded_attachment_type")
    var embeddedAttachmentType: String? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("from_user_avatar_thumb")
    var fromUserAvatarThumb: String? = null,

    @field:SerializedName("from_user_avatar_medium")
    var fromUserAvatarMedium: String? = null,

    @field:SerializedName("message_updated_at")
    var messageUpdatedAt: String? = null,

    @field:SerializedName("picture")
    var picture: String? = null,

    @field:SerializedName("chat_id")
    var chatId: Int,

    @field:SerializedName("from_user_id")
    var fromUserId: Int? = null
) {
    constructor() : this(
        0,
        "",
        "",
        "",
        false,
        "",
        "",
        "",
        "",
        "",
        "",
        -1,
        -1
    )

    @Ignore
    var attachment: Any? = null

    fun getCheckedPicture(): String? {
        return if (picture == "https://apinew.pincapp.com/images/default_avatar.png")
            null
        else
            picture
    }
}