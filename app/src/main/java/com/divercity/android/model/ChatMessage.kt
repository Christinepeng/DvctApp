package com.divercity.android.model

/**
 * Created by lucas on 2019-04-24.
 */

data class ChatMessage(
    var id: Int,
    var messageCreatedAt: String? = null,
    var embeddedAttachmentId: String? = null,
    var fromUsername: String? = null,
    var hasEmbeddedAttachment: Boolean? = null,
    var embeddedAttachmentType: String? = null,
    var message: String? = null,
    var fromUserAvatarThumb: String? = null,
    var fromUserAvatarMedium: String? = null,
    var messageUpdatedAt: String? = null,
    var picture: String? = null,
    var chatId: Int,
    var fromUserId: Int? = null
) {

    fun checkedPicture(): String? {
        return if (picture == "https://apinew.pincapp.com/images/default_avatar.png")
            null
        else
            picture
    }
}