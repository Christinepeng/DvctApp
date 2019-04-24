package com.divercity.android.data.entity.company.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CompanyResponse(

    @field:SerializedName("attributes")
    var attributes: Attributes? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Attributes::class.java.classLoader)!!,
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

    companion object CREATOR : Parcelable.Creator<CompanyResponse> {
        override fun createFromParcel(parcel: Parcel): CompanyResponse {
            return CompanyResponse(parcel)
        }

        override fun newArray(size: Int): Array<CompanyResponse?> {
            return arrayOfNulls(size)
        }
    }

}