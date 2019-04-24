package com.divercity.android.data.entity.company.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

    @field:SerializedName("zip")
    val zip: String? = null,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("industry")
    val industry: String? = null,

    @field:SerializedName("photos")
    val photos: Photos? = null,

    @field:SerializedName("current_user_admin")
    val currentUserAdmin: Boolean? = null,

    @field:SerializedName("cover_photos")
    val coverPhotos: CoverPhotos? = null,

    @field:SerializedName("headquarters")
    val headquarters: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("company_size")
    val companySize: String? = null,

    @field:SerializedName("divercity_rating")
    val divercityRating: Int? = null,

    @field:SerializedName("total_divercity_rating")
    val totalDivercityRating: Int? = null,

    @field:SerializedName("can_rate_company")
    val canRateCompany: Boolean? = null

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        null,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        null,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(zip)
        parcel.writeString(country)
        parcel.writeString(address)
        parcel.writeString(description)
        parcel.writeString(photo)
        parcel.writeString(industry)
        parcel.writeValue(currentUserAdmin)
        parcel.writeString(headquarters)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(companySize)
        parcel.writeValue(divercityRating)
        parcel.writeValue(totalDivercityRating)
        parcel.writeValue(canRateCompany)
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