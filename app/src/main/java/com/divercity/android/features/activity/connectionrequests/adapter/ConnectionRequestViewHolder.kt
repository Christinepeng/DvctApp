package com.divercity.android.features.activity.connectionrequests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.activity.connectionrequests.model.GroupInvitationNotificationPosition
import com.divercity.android.features.activity.connectionrequests.model.JoinGroupRequestPosition
import com.divercity.android.features.activity.connectionrequests.model.UserPosition
import kotlinx.android.synthetic.main.item_connection_request.view.*

class ConnectionRequestViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?
) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: ConnectionItem?) {

        data?.let {
            itemView.apply {

                when (data) {
                    is GroupInvitationNotificationResponse -> {
                        val groupInvitation = data as GroupInvitationNotificationResponse
                        txt_notification.text = groupInvitation.attributes?.body

                        GlideApp.with(this)
                            .load(groupInvitation.attributes?.inviteeInfo?.avatarMedium)
                            .apply(RequestOptions().circleCrop())
                            .into(img_user)

                        when (groupInvitation.attributes?.status) {
                            "pending" -> {
                                btn_accept.visibility = View.VISIBLE
                                btn_decline.visibility = View.VISIBLE
                                img_result.visibility = View.INVISIBLE

                                btn_accept.setOnClickListener {
                                    listener?.acceptGroupInvitation(
                                        GroupInvitationNotificationPosition(
                                            position,
                                            groupInvitation
                                        )
                                    )
                                }

                                btn_decline.setOnClickListener {
                                    listener?.declineGroupInvitation(
                                        GroupInvitationNotificationPosition(
                                            position,
                                            groupInvitation
                                        )
                                    )
                                }
                            }
                            itemView.context.getString(R.string.accepted) -> {
                                btn_accept.visibility = View.INVISIBLE
                                btn_decline.visibility = View.INVISIBLE
                                img_result.visibility = View.VISIBLE
                                img_result.setImageResource(R.drawable.icon_connected)

                                btn_accept.setOnClickListener(null)
                                btn_decline.setOnClickListener(null)
                            }
                            itemView.context.getString(R.string.declined) -> {
                                btn_accept.visibility = View.INVISIBLE
                                btn_decline.visibility = View.INVISIBLE
                                img_result.visibility = View.VISIBLE
                                img_result.setImageResource(R.drawable.icon_decline)

                                btn_accept.setOnClickListener(null)
                                btn_decline.setOnClickListener(null)
                            }
                        }
                    }
                    is JoinGroupRequestResponse -> {
                        val joinGroup = data as JoinGroupRequestResponse
                        txt_notification.text =
                            "${joinGroup.attributes?.userInfo?.name} " +
                                    "wants to join your group: " +
                                    "${joinGroup.attributes?.groupInfo?.title}"

                        GlideApp.with(this)
                            .load(joinGroup.attributes?.userInfo?.avatarMedium)
                            .apply(RequestOptions().circleCrop())
                            .into(img_user)

                        when (joinGroup.attributes?.state) {
                            "pending" -> {
                                btn_accept.visibility = View.VISIBLE
                                btn_decline.visibility = View.VISIBLE
                                img_result.visibility = View.INVISIBLE

                                btn_accept.setOnClickListener {
                                    listener?.acceptJoinGroupRequest(
                                        JoinGroupRequestPosition(position, joinGroup)
                                    )
                                }

                                btn_decline.setOnClickListener {
                                    listener?.declineJoinGroupRequest(
                                        JoinGroupRequestPosition(position, joinGroup)
                                    )
                                }
                            }
                            itemView.context.getString(R.string.accepted) -> {
                                btn_accept.visibility = View.INVISIBLE
                                btn_decline.visibility = View.INVISIBLE
                                img_result.visibility = View.VISIBLE

                                img_result.setImageResource(R.drawable.icon_connected)
                            }
                            itemView.context.getString(R.string.declined) -> {
                                btn_accept.visibility = View.INVISIBLE
                                btn_decline.visibility = View.INVISIBLE
                                img_result.visibility = View.VISIBLE

                                img_result.setImageResource(R.drawable.icon_decline)
                            }
                        }
                    }
                    is UserResponse -> {
                        val connectionRequest = data as UserResponse
                        txt_notification.text =
                            "${connectionRequest.userAttributes?.name} wants to connect with you"

                        GlideApp.with(this)
                            .load(connectionRequest.userAttributes?.avatarMedium)
                            .apply(RequestOptions().circleCrop())
                            .into(img_user)

                        if (connectionRequest.userAttributes?.connected == "pending_approval") {
                            btn_accept.visibility = View.VISIBLE
                            btn_decline.visibility = View.VISIBLE
                            img_result.visibility = View.INVISIBLE

                            btn_accept.setOnClickListener {
                                listener?.acceptUserConnectionRequest(
                                    UserPosition(position, connectionRequest)
                                )
                            }

                            btn_decline.setOnClickListener {
                                listener?.declineUserConnectionRequest(
                                    UserPosition(position, connectionRequest)
                                )
                            }
                        } else if (connectionRequest.userAttributes?.connected == "accepted") {
                            btn_accept.visibility = View.INVISIBLE
                            btn_decline.visibility = View.INVISIBLE
                            img_result.visibility = View.VISIBLE

                            img_result.setImageResource(R.drawable.icon_connected)
                        } else if (connectionRequest.userAttributes?.connected == "declined") {
                            btn_accept.visibility = View.INVISIBLE
                            btn_decline.visibility = View.INVISIBLE
                            img_result.visibility = View.VISIBLE

                            img_result.setImageResource(R.drawable.icon_decline)
                        }
                    }
                }
            }
        }
    }

    interface Listener {

        fun acceptGroupInvitation(invitation: GroupInvitationNotificationPosition)

        fun declineGroupInvitation(invitation: GroupInvitationNotificationPosition)

        fun acceptJoinGroupRequest(request: JoinGroupRequestPosition)

        fun declineJoinGroupRequest(request: JoinGroupRequestPosition)

        fun acceptUserConnectionRequest(user: UserPosition)

        fun declineUserConnectionRequest(user: UserPosition)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?
        ): ConnectionRequestViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_connection_request, parent, false)
            return ConnectionRequestViewHolder(view, listener)
        }
    }
}
