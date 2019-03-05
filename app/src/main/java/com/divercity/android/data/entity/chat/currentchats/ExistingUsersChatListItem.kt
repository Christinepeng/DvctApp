package com.divercity.android.data.entity.chat.currentchats

import androidx.room.Entity
import androidx.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import com.divercity.android.data.entity.createchat.UsersItem
import com.divercity.android.db.converter.DateTypeConverter
import com.divercity.android.db.converter.UserItemListConverter
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "recentChat",primaryKeys = ["chatId"])
@TypeConverters(UserItemListConverter::class, DateTypeConverter::class)
data class ExistingUsersChatListItem(

    @field:SerializedName("last_message_date")
    var lastMessageDate: Date? = Date(),

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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        Date(parcel.readLong()),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(UsersItem),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(lastMessageDate!!.time)
        parcel.writeString(avatarMedium)
        parcel.writeString(chatPicture)
        parcel.writeString(avatarThumb)
        parcel.writeValue(blockedByMe)
        parcel.writeString(lastMessage)
        parcel.writeValue(unreadMessageCount)
        parcel.writeInt(chatId)
        parcel.writeString(chatType)
        parcel.writeValue(blocked)
        parcel.writeValue(notificationMuted)
        parcel.writeString(nickname)
        parcel.writeString(name)
        parcel.writeTypedList(chatUsers)
        parcel.writeValue(id)
        parcel.writeString(email)
        parcel.writeString(lastMessagePicture)
        parcel.writeString(chatName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExistingUsersChatListItem> {
        override fun createFromParcel(parcel: Parcel): ExistingUsersChatListItem {
            return ExistingUsersChatListItem(parcel)
        }

        override fun newArray(size: Int): Array<ExistingUsersChatListItem?> {
            return arrayOfNulls(size)
        }
    }

    fun getCheckedPicture(): String? {
        return if (lastMessagePicture == "https://apinew.pincapp.com/images/default_avatar.png")
            null
        else
            lastMessagePicture
    }
}