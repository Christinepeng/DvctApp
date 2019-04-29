package com.divercity.android.model.usermentionable

import com.linkedin.android.spyglass.tokenization.QueryToken

/**
 * Created by lucas on 2019-04-29.
 */

data class QueryTokenUserMentionable(
    val queryToken: QueryToken,
    val users: List<UserMentionable>
)