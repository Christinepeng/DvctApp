package com.divercity.app.data.entity.location

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LocationResponse(

    @field:SerializedName("attributes")
    val attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Attributes::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(attributes, flags)
        parcel.writeString(id)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationResponse> {
        override fun createFromParcel(parcel: Parcel): LocationResponse {
            return LocationResponse(parcel)
        }

        override fun newArray(size: Int): Array<LocationResponse?> {
            return arrayOfNulls(size)
        }
    }
}