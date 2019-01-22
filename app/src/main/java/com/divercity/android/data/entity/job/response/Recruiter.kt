package com.divercity.android.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Recruiter(

	@field:SerializedName("avatar_medium")
	val avatarMedium: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("avatar_thumb")
	val avatarThumb: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("present")
	val present: Boolean? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Int::class.java.classLoader) as? Int,
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(avatarMedium)
		parcel.writeString(occupation)
		parcel.writeString(avatarThumb)
		parcel.writeString(name)
		parcel.writeValue(id)
		parcel.writeValue(present)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Recruiter> {
		override fun createFromParcel(parcel: Parcel): Recruiter {
			return Recruiter(parcel)
		}

		override fun newArray(size: Int): Array<Recruiter?> {
			return arrayOfNulls(size)
		}
	}
}