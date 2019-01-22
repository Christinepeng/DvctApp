package com.divercity.android.data.entity.createchat

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UsersItem(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("id")
	var id: String? = null
) : Parcelable {

	constructor(parcel: Parcel) : this(
			parcel.readString(),
			parcel.readString())

	constructor() : this("", "0")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(name)
		parcel.writeString(id)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<UsersItem> {
		override fun createFromParcel(parcel: Parcel): UsersItem {
			return UsersItem(parcel)
		}

		override fun newArray(size: Int): Array<UsersItem?> {
			return arrayOfNulls(size)
		}
	}
}