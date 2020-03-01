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
    var companyId: Int?,
    var companyName: String?,
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
                (accountType == context.getString(R.string.professional_job_seeker_id) ||
                        accountType == context.getString(R.string.student_id) ||
                        accountType == context.getString(R.string.professional_id))
    }

    fun fullLocation(): String? {
        return if (city != null)
            city?.plus(", ").plus(country)
        else
            country
    }

    fun copy(user: User) {
        country = user.country
        avatarMedium = user.avatarThumb
        accountType = user.accountType
        occupation = user.occupation
        birthdate = user.birthdate
        role = user.role
        gender = user.gender
        ethnicity = user.ethnicity
        city = user.city
        timezone = user.timezone
        phoneNumber = user.phoneNumber
        createdAt = user.createdAt
        questionsCount = user.questionsCount
        occupationOfInterests = user.occupationOfInterests
        groupOfInterestFollowingCount = user.groupOfInterestFollowingCount
        uid = user.uid
        answersCount = user.answersCount
        nickname = user.nickname
        companyId = user.companyId
        companyName = user.companyName
        isFollowedByCurrent = user.isFollowedByCurrent
        email = user.email
        noPasswordSet = user.noPasswordSet
        avatarThumb = user.avatarThumb
        lastName = user.lastName
        schoolName = user.schoolName
        isDefaultAvatar = user.isDefaultAvatar
        interestIds = user.interestIds
        followingCount = user.followingCount
        followersCount = user.followersCount
        industries = user.industries
        studentMajors = user.studentMajors
        name = user.name
        ageRange = user.ageRange
        skills = user.skills
        connected = user.connected
    }
}