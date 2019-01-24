package com.divercity.android.features.chat.chat.model

import android.os.Parcel
import android.os.Parcelable
import com.linkedin.android.spyglass.mentions.Mentionable

/**
 * Created by lucas on 24/01/2019.
 */

data class ChatMember(
    var userId: String?,
    var userName: String?,
    var urlImg: String?,
    var occupation: String?,
    var startPosition: Int,
    var endPosition: Int
) : Mentionable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun getSuggestibleId(): Int {
        return userName.hashCode()
    }

    override fun getSuggestiblePrimaryText(): String {
        return userName!!
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.FULL_DELETE;
    }

    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode?): String {
        return when (mode) {
            Mentionable.MentionDisplayMode.FULL -> "@".plus(userName)
            Mentionable.MentionDisplayMode.PARTIAL -> ""
            Mentionable.MentionDisplayMode.NONE -> ""
            else -> ""
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeString(urlImg)
        parcel.writeString(occupation)
        parcel.writeInt(startPosition)
        parcel.writeInt(endPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatMember> {
        override fun createFromParcel(parcel: Parcel): ChatMember {
            return ChatMember(parcel)
        }

        override fun newArray(size: Int): Array<ChatMember?> {
            return arrayOfNulls(size)
        }
    }
}