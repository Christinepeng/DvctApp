package com.divercity.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by lucas on 2019-06-07.
 */

@Parcelize
data class School(
    val id: String,
    var name: String?,
    var logo: String?
) : Parcelable