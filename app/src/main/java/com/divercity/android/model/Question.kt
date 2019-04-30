package com.divercity.android.model

import android.os.Parcel
import android.os.Parcelable
import com.divercity.android.data.entity.home.HomeItem

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
    var groupId: Int?,
    var authorId: String?,
    var lastAnswer: String?,
    var lastAnswerAuthorName: String?,
    var lastAnswerAuthorPicture: String?
) : HomeItem, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
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
        parcel.writeString(authorId)
        parcel.writeString(lastAnswer)
        parcel.writeString(lastAnswerAuthorName)
        parcel.writeString(lastAnswerAuthorPicture)
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