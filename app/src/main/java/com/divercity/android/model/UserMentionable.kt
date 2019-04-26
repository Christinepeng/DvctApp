package com.divercity.android.model

import android.os.Parcel
import android.os.Parcelable
import com.linkedin.android.spyglass.mentions.Mentionable

/**
 * Created by lucas on 2019-04-24.
 */

data class UserMentionable(
    var userId: String,
    var fullName: String,
    var pictureUrl: String
) : Mentionable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun getSuggestibleId(): Int {
        return userId.hashCode()
    }

    override fun getSuggestiblePrimaryText(): String {
        return fullName
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.FULL_DELETE
    }

    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode?): String {
        return when (mode) {
            Mentionable.MentionDisplayMode.FULL -> fullName
            Mentionable.MentionDisplayMode.PARTIAL -> {
                val words = fullName.split(" ")
                if (words.size > 1) words[0] else ""
            }
            Mentionable.MentionDisplayMode.NONE -> ""
            else -> ""
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(fullName)
        parcel.writeString(pictureUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserMentionable> {
        override fun createFromParcel(parcel: Parcel): UserMentionable {
            return UserMentionable(parcel)
        }

        override fun newArray(size: Int): Array<UserMentionable?> {
            return arrayOfNulls(size)
        }
    }

}