package com.divercity.android.model

import java.util.*

/**
 * Created by lucas on 2019-07-08.
 */

    data class CompanyDiversityReview(
    val id: String,
    val raceEthnicityRate: Int? = null,
    val sexualOrientationRate: Int? = null,
    val rate: Int? = null,
    val genderRate: Int? = null,
    val ableBodiednessRate: Int? = null,
    val review: String? = null,
    val ageRate: Int? = null,
    val createdAt: Date? = null,
    var isRatingExpanded: Boolean = false,
    var isReviewExpanded: Boolean = false
)