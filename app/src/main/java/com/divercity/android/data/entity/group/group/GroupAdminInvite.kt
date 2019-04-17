package com.divercity.android.data.entity.group.group

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GroupAdminInvite(

    @field:SerializedName("invite_id")
    val inviteId: Int? = null

) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(inviteId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GroupAdminInvite> {
        override fun createFromParcel(parcel: Parcel): GroupAdminInvite {
            return GroupAdminInvite(parcel)
        }

        override fun newArray(size: Int): Array<GroupAdminInvite?> {
            return arrayOfNulls(size)
        }
    }

}