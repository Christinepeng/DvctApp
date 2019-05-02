package com.divercity.android.data.entity.group.group

import android.os.Parcel
import android.os.Parcelable
import com.divercity.android.data.entity.home.RecommendedItem
import com.google.gson.annotations.SerializedName

data class GroupResponse(

    @field:SerializedName("attributes")
    var attributes: Attributes = Attributes(),

    @field:SerializedName("id")
    var id: String = "-1"

) : RecommendedItem, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Attributes::class.java.classLoader)!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(attributes, flags)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupResponse> {
        override fun createFromParcel(parcel: Parcel): GroupResponse {
            return GroupResponse(parcel)
        }

        override fun newArray(size: Int): Array<GroupResponse?> {
            return arrayOfNulls(size)
        }
    }

    fun isPublic(): Boolean {
        return attributes.groupType?.toLowerCase() == "public"
    }

    fun isJoinRequestPending(): Boolean {
        return attributes.requestToJoinStatus == "pending"
    }

    fun isJoinRequestNotSend(): Boolean {
        return attributes.requestToJoinStatus == "none"
    }
}