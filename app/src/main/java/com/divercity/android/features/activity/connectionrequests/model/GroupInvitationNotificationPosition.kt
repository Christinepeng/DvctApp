package com.divercity.android.features.activity.connectionrequests.model

import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse

/**
 * Created by lucas on 27/03/2019.
 */

data class GroupInvitationNotificationPosition(
    var position: Int,
    var groupInvitation: GroupInvitationNotificationResponse
)