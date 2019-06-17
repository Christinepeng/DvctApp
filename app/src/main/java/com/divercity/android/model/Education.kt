package com.divercity.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by lucas on 2019-06-05.
 */

@Parcelize
data class Education(
    var id: String,
    val major: String?,
    val schoolName: String?,
    val startYear: String?,
    val endYear: String?
) : Parcelable