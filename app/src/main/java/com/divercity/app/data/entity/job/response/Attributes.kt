package com.divercity.app.data.entity.job.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Attributes(

	@field:SerializedName("is_bookmarked_by_current")
	var isBookmarkedByCurrent: Boolean? = null,

	@field:SerializedName("images")
	val images: Images? = null,

	@field:SerializedName("recruiter")
	val recruiter: Recruiter? = null,

	@field:SerializedName("published_on")
	val publishedOn: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("is_applied_by_current")
	val isAppliedByCurrent: Boolean? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("publishable")
	var publishable: Boolean? = null,

	@field:SerializedName("unpublished_on")
	val unpublishedOn: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("location_display_name")
	val locationDisplayName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("job_type_info")
	val jobTypeInfo: JobTypeInfo? = null,

	@field:SerializedName("employer")
	val employer: Employer? = null,

	@field:SerializedName("skills_tag")
	val skillsTag: List<String?>? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readParcelable(Images::class.java.classLoader),
			parcel.readParcelable(Recruiter::class.java.classLoader),
			parcel.readString(),
			parcel.readString(),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readString(),
			parcel.readParcelable(JobTypeInfo::class.java.classLoader),
			parcel.readParcelable(Employer::class.java.classLoader),
			parcel.createStringArrayList()) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeValue(isBookmarkedByCurrent)
		parcel.writeParcelable(images, flags)
		parcel.writeParcelable(recruiter, flags)
		parcel.writeString(publishedOn)
		parcel.writeString(description)
		parcel.writeValue(isAppliedByCurrent)
		parcel.writeString(createdAt)
		parcel.writeValue(publishable)
		parcel.writeString(unpublishedOn)
		parcel.writeString(title)
		parcel.writeString(locationDisplayName)
		parcel.writeString(updatedAt)
		parcel.writeParcelable(jobTypeInfo, flags)
		parcel.writeParcelable(employer, flags)
		parcel.writeStringList(skillsTag)
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