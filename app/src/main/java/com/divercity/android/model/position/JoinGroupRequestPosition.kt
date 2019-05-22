package com.divercity.android.model.position

import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse

/**
 * Created by lucas on 27/03/2019.
 */

data class JoinGroupRequestPosition(
    var position: Int,
    var joinGroupRequest: JoinGroupRequestResponse
)