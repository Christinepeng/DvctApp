package com.divercity.android.core.extension

import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.data.entity.degree.DegreeEntityResponse
import com.divercity.android.data.entity.education.response.EducationEntityResponse
import com.divercity.android.model.CompanyDiversityReview
import com.divercity.android.model.Degree
import com.divercity.android.model.Education

/**
 * Created by lucas on 2019-06-12.
 */

fun EducationEntityResponse.toEducation() = Education(
    id,
    attributes?.qualification,
    attributes?.schoolInfo?.id,
    attributes?.startYear,
    attributes?.endYear
)

fun DegreeEntityResponse.toDegree() = Degree(
    id,
    attributes.name
)

fun CompanyDiversityReviewEntityResponse.toCompanyDiverseReview() = CompanyDiversityReview(
    id,
    attributes?.raceEthnicityRate,
    attributes?.sexualOrientationRate,
    attributes?.rate,
    attributes?.genderRate,
    attributes?.ableBodiednessRate,
    attributes?.review,
    attributes?.ageRate,
    attributes?.createdAt
)