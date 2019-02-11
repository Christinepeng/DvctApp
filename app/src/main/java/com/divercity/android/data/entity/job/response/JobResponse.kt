package com.divercity.android.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.divercity.android.data.entity.home.HomeItem
import com.divercity.android.data.entity.home.RecommendedItem
import com.google.gson.annotations.SerializedName

data class JobResponse(

	@field:SerializedName("attributes")
	val attributes: Attributes? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) : RecommendedItem, HomeItem, Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readParcelable(Attributes::class.java.classLoader),
			parcel.readString(),
			parcel.readString())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeParcelable(attributes, flags)
		parcel.writeString(id)
		parcel.writeString(type)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<JobResponse> {
		override fun createFromParcel(parcel: Parcel): JobResponse {
			return JobResponse(parcel)
		}

		override fun newArray(size: Int): Array<JobResponse?> {
			return arrayOfNulls(size)
		}
	}
}