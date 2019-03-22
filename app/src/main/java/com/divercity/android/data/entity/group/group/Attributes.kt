package com.divercity.android.data.entity.group.group

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

    @field:SerializedName("current_user_admin_level")
    var currentUserAdminLevel: String? = null,

    @field:SerializedName("color")
    var color: String? = null,

    @field:SerializedName("questions_count")
    var questionsCount: Int? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("created_at")
    var createdAt: Int? = null,

    @field:SerializedName("group_type")
    var groupType: String? = null,

    @field:SerializedName("title")
    var title: String? = null,

    @field:SerializedName("is_current_user_admin")
    var isCurrentUserAdmin: Boolean = false,

    @field:SerializedName("answers_count")
    var answersCount: Int? = null,

    @field:SerializedName("followers_count")
    var followersCount: Int = 0,

    @field:SerializedName("state")
    var state: String? = null,

    @field:SerializedName("is_followed_by_current")
    var isFollowedByCurrent: Boolean = false,

    @field:SerializedName("picture_main")
    var pictureMain: String? = null,

    @field:SerializedName("author_info")
    var authorInfo: AuthorInfo? = null,

    @field:SerializedName("request_to_join_status")
    var requestToJoinStatus: String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readParcelable(AuthorInfo::class.java.classLoader),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currentUserAdminLevel)
        parcel.writeString(color)
        parcel.writeValue(questionsCount)
        parcel.writeString(description)
        parcel.writeValue(createdAt)
        parcel.writeString(groupType)
        parcel.writeString(title)
        parcel.writeByte(if (isCurrentUserAdmin) 1 else 0)
        parcel.writeValue(answersCount)
        parcel.writeInt(followersCount)
        parcel.writeString(state)
        parcel.writeByte(if (isFollowedByCurrent) 1 else 0)
        parcel.writeString(pictureMain)
        parcel.writeParcelable(authorInfo, flags)
        parcel.writeString(requestToJoinStatus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attributes> {
        override fun createFromParcel(parcel: Parcel): Attributes {
            return Attributes(parcel)
        }

        override fun newArray(size: Int): Array<Attributes?> {
            return arrayOfNulls(size)
        }
    }
}