package com.divercity.app.data.entity.company.sizes

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("name")
	val name: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readString()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
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