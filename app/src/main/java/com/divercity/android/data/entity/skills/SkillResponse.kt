package com.divercity.android.data.entity.skills

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SkillResponse(

        @field:SerializedName("attributes")
        val attributes: Attributes? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

// For ViewHolder
        var isSelected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Attributes::class.java.classLoader),
            parcel.readString(),
            isSelected = parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(attributes, flags)
        parcel.writeString(id)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SkillResponse> {
        override fun createFromParcel(parcel: Parcel): SkillResponse {
            return SkillResponse(parcel)
        }

        override fun newArray(size: Int): Array<SkillResponse?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is SkillResponse && attributes != null) {
            return attributes.name == other.attributes?.name
        }
        return false
    }

    override fun toString(): String {
        return attributes?.name ?: ""
    }
}