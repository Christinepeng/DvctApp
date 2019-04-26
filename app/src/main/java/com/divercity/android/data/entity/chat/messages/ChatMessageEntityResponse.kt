package com.divercity.android.data.entity.chat.messages

import androidx.room.Entity
import androidx.room.Ignore
import com.divercity.android.core.extension.empty
import com.google.gson.annotations.SerializedName

@Entity(tableName = "chatMessage", primaryKeys = ["id", "chatId"])
data class ChatMessageEntityResponse(

    @field:SerializedName("id")
    var id: Int = -1,

    @field:SerializedName("message_created_at")
    var messageCreatedAt: String? = String.empty(),

    @field:SerializedName("embedded_attachment_id")
    var embeddedAttachmentId: String? = String.empty(),

    @field:SerializedName("from_username")
    var fromUsername: String? = String.empty(),

    @field:SerializedName("has_embedded_attachment")
    var hasEmbeddedAttachment: Boolean? = false,

    @field:SerializedName("embedded_attachment_type")
    var embeddedAttachmentType: String? = String.empty(),

    @field:SerializedName("message")
    var message: String? = String.empty(),

    @field:SerializedName("from_user_avatar_thumb")
    var fromUserAvatarThumb: String? = String.empty(),

    @field:SerializedName("from_user_avatar_medium")
    var fromUserAvatarMedium: String? = String.empty(),

    @field:SerializedName("message_updated_at")
    var messageUpdatedAt: String? = String.empty(),

    @field:SerializedName("picture")
    var picture: String? = String.empty(),

    @field:SerializedName("chat_id")
    var chatId: Int = -1,

    @field:SerializedName("from_user_id")
    var fromUserId: Int? = -1,

    @Ignore
    var attachment: Any? = null
) {

    fun getCheckedPicture(): String? {
        return if (picture == "https://apinew.pincapp.com/images/default_avatar.png")
            null
        else
            picture
    }
}