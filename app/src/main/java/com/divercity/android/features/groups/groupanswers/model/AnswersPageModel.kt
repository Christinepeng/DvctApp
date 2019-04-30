package com.divercity.android.features.groups.groupanswers.model

import com.divercity.android.data.entity.group.answer.response.AnswerEntityResponse

/**
 * Created by lucas on 10/04/2019.
 */

data class AnswersPageModel(val answers: List<AnswerEntityResponse>, val page: Int)