package com.divercity.android.model.user

import android.content.Context
import com.divercity.android.R
import com.divercity.android.data.entity.group.ConnectionItem

/**
 * Created by lucas on 2019-04-24.
 */

data class User(
    val id: String,
    var country: String?,
    var avatarMedium: String?,
    var accountType: String?,
    var occupation: String?,
    var birthdate: String?,
    var role: String?,
    var gender: String?,
    var ethnicity: String?,
    var city: String?,
    var timezone: String?,
    var phoneNumber: String?,
    var createdAt: String?,
    var questionsCount: Int?,
    var occupationOfInterests: List<String>?,
    var groupOfInterestFollowingCount: Int?,
    var uid: String?,
    var answersCount: Int?,
    var nickname: String?,
    var company: UserCompany?,
    var isFollowedByCurrent: Boolean?,
    var email: String?,
    var noPasswordSet: String?,
    var avatarThumb: String?,
    var lastName: String?,
    var schoolName: String?,
    var isDefaultAvatar: Boolean?,
    var interestIds: List<Int>?,
    var followingCount: Int?,
    var followersCount: Int?,
    var industries: List<String>?,
    var studentMajors: List<String>?,
    var name: String?,
    var ageRange: String?,
    var skills: List<String>?,
    var connected: String?
) : ConnectionItem {

    fun isJobSeeker(context: Context): Boolean {
        return accountType != null &&
                (accountType == context.getString(R.string.job_seeker_id) ||
                        accountType == context.getString(R.string.student_id) ||
                        accountType == context.getString(R.string.professional_id))
    }

    fun fullLocation(): String {
        return city.plus(", ").plus(country)
    }
}