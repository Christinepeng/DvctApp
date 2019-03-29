package com.divercity.android.features.activity.connectionrequests.model

import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse

/**
 * Created by lucas on 27/03/2019.
 */

data class JoinGroupRequestPosition(
    var position: Int,
    var joinGroupRequest: JoinGroupRequestResponse
)