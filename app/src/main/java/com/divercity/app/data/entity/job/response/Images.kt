package com.divercity.app.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Images(

	@field:SerializedName("thumb")
	val thumb: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(thumb)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Images> {
		override fun createFromParcel(parcel: Parcel): Images {
			return Images(parcel)
		}

		override fun newArray(size: Int): Array<Images?> {
			return arrayOfNulls(size)
		}
	}
}