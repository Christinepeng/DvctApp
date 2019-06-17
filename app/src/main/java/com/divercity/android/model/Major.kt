package com.divercity.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by lucas on 2019-06-10.
 */

@Parcelize
data class Major(
    val id: String,
    var name: String?
) : Parcelable