package com.divercity.app.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

        @field:SerializedName("is_bookmarked_by_current")
        var isBookmarkedByCurrent: Boolean? = null,

        @field:SerializedName("images")
        val images: Images? = null,

        @field:SerializedName("updated_at")
        val updatedAt: String? = null,

        @field:SerializedName("job_type_info")
        val jobTypeInfo: JobTypeInfo? = null,

        @field:SerializedName("recruiter")
        val recruiter: Recruiter? = null,

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("employer")
        val employer: Employer? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("title")
        val title: String? = null,

        @field:SerializedName("deadline")
        val deadline: String? = null,

        @field:SerializedName("location_display_name")
        val locationDisplayName: String? = null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
                parcel.readParcelable(Images::class.java.classLoader),
                parcel.readString(),
                parcel.readParcelable(JobTypeInfo::class.java.classLoader),
                parcel.readParcelable(Recruiter::class.java.classLoader),
                parcel.readString(),
                parcel.readParcelable(Employer::class.java.classLoader),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(isBookmarkedByCurrent)
                parcel.writeParcelable(images, flags)
                parcel.writeString(updatedAt)
                parcel.writeParcelable(jobTypeInfo, flags)
                parcel.writeParcelable(recruiter, flags)
                parcel.writeString(description)
                parcel.writeParcelable(employer, flags)
                parcel.writeString(createdAt)
                parcel.writeString(title)
                parcel.writeString(deadline)
                parcel.writeString(locationDisplayName)
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