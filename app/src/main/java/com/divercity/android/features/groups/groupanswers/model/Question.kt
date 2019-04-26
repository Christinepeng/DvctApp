package com.divercity.android.features.groups.groupanswers.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by lucas on 24/01/2019.
 */

data class Question(
    var id: String?,
    var question: String?,
    var questionPicUrl: String?,
    var createdAt: String?,
    var authorProfilePicUrl: String?,
    var authorName: String?,
    var groupTitle: String?,
    var groupId: Int?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(question)
        parcel.writeString(questionPicUrl)
        parcel.writeString(createdAt)
        parcel.writeString(authorProfilePicUrl)
        parcel.writeString(authorName)
        parcel.writeString(groupTitle)
        parcel.writeValue(groupId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}