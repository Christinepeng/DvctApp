package com.divercity.android.features.groups.answers.model

import com.divercity.android.data.entity.group.answer.response.AnswerResponse

/**
 * Created by lucas on 10/04/2019.
 */

data class AnswersPageModel(val answers: List<AnswerResponse>, val page: Int)