package com.divercity.android.core.extension

import com.divercity.android.data.entity.degree.DegreeEntityResponse
import com.divercity.android.data.entity.education.response.EducationEntityResponse
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