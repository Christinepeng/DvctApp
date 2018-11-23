package com.divercity.app.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CoverPhotos(

	@field:SerializedName("original")
	val original: String? = null,

	@field:SerializedName("thumb")
	val thumb: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(original)
		parcel.writeString(thumb)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<CoverPhotos> {
		override fun createFromParcel(parcel: Parcel): CoverPhotos {
			return CoverPhotos(parcel)
		}

		override fun newArray(size: Int): Array<CoverPhotos?> {
			return arrayOfNulls(size)
		}
	}
}