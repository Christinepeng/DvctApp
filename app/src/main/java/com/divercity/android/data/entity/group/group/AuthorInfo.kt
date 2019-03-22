package com.divercity.android.data.entity.group.group

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AuthorInfo(

    @field:SerializedName("avatar_medium")
    val avatarMedium: String? = null,

    @field:SerializedName("avatar_thumb")
    val avatarThumb: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("present")
    val present: Boolean? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatarMedium)
        parcel.writeString(avatarThumb)
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeValue(id)
        parcel.writeValue(present)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthorInfo> {
        override fun createFromParcel(parcel: Parcel): AuthorInfo {
            return AuthorInfo(parcel)
        }

        override fun newArray(size: Int): Array<AuthorInfo?> {
            return arrayOfNulls(size)
        }
    }
}