package com.divercity.android.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class JobTypeInfo(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeValue(id)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<JobTypeInfo> {
		override fun createFromParcel(parcel: Parcel): JobTypeInfo {
			return JobTypeInfo(parcel)
		}

		override fun newArray(size: Int): Array<JobTypeInfo?> {
			return arrayOfNulls(size)
		}
	}
}