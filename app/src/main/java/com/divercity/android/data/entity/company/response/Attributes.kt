package com.divercity.android.data.entity.company.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
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

	@field:SerializedName("current_user_admin")
	val currentUserAdmin: Boolean? = null,

	@field:SerializedName("headquarters")
	val headquarters: String? = null,

	@field:SerializedName("can_rate_company")
	val canRateCompany: Boolean? = null,

	@field:SerializedName("has_rated_before")
	val hasRatedBefore: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("company_size")
	val companySize: String? = null
) : Parcelable {

	@IgnoredOnParcel
	@field:SerializedName("industry_info")
	val industryInfo: IndustryInfo? = null

	@IgnoredOnParcel
	@field:SerializedName("company_size_info")
	val companySizeInfo: CompanySizeInfo? = null

	@IgnoredOnParcel
	@field:SerializedName("photos")
	val photos: Photos? = null

	@IgnoredOnParcel
	@field:SerializedName("cover_photos")
	val coverPhotos: CoverPhotos? = null

	@IgnoredOnParcel
	@field:SerializedName("divercity_rating")
	val divercityRating: DivercityRating? = null
}