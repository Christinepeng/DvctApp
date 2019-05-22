package com.divercity.android.model.position

import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse

/**
 * Created by lucas on 27/03/2019.
 */

data class GroupInvitationNotificationPosition(
    var position: Int,
    var groupInvitation: GroupInvitationNotificationResponse
)