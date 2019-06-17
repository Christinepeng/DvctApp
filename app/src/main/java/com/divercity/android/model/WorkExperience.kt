package com.divercity.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by lucas on 2019-06-06.
 */

@Parcelize
data class WorkExperience(
    var id: String,
    var companyName: String?,
    var companyPic: String?,
    var role: String?,
    var startDate: String?,
    var endDate: String?,
    var isPresent: Boolean?,
    var experienceDetails: String?
) : Parcelable
