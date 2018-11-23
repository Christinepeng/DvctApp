package com.divercity.app.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Employer(

	@field:SerializedName("cover_photos")
	val coverPhotos: CoverPhotos? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("photos")
	val photos: Photos? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(CoverPhotos::class.java.classLoader),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readParcelable(Photos::class.java.classLoader)) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(coverPhotos, flags)
		parcel.writeString(name)
		parcel.writeValue(id)
		parcel.writeParcelable(photos, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Employer> {
		override fun createFromParcel(parcel: Parcel): Employer {
			return Employer(parcel)
		}

		override fun newArray(size: Int): Array<Employer?> {
			return arrayOfNulls(size)
		}
	}
}