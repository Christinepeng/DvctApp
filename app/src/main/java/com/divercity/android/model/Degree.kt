package com.divercity.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by lucas on 2019-06-18.
 */

@Parcelize
data class Degree(
    var id: String,
    var name: String
) : Parcelable