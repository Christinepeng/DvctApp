package com.divercity.android.features.activity.connectionrequests.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.group.ConnectionItem
import com.divercity.android.data.entity.group.invitationnotification.GroupInvitationNotificationResponse
import com.divercity.android.data.entity.group.requests.JoinGroupRequestResponse
import kotlinx.android.synthetic.main.item_connection_request.view.*

class ConnectionRequestViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?
) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: ConnectionItem?) {
        data?.let {
            itemView.apply {

                when(data){
                    is GroupInvitationNotificationResponse -> {
                        val groupInvitation = data as GroupInvitationNotificationResponse
                        txt_notification.text = groupInvitation.attributes?.body
                    }
                    is JoinGroupRequestResponse -> {
                        val joinGroup = data as JoinGroupRequestResponse
                        txt_notification.text = "This is a join group request"
                    }
                }

//                GlideApp.with(this)
//                    .load(it.attributes?.lastActiveUserInfo?.avatarThumb)
//                    .apply(RequestOptions().circleCrop())
//                    .into(img_user)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    txt_notification.text =
//                        Html.fromHtml(data.attributes?.message, Html.FROM_HTML_MODE_COMPACT)
//                else
//                    txt_notification.text = Html.fromHtml(data.attributes?.message)
//
////                txt_notification.typeface = ResourcesCompat.getFont(this.context, R.font.avenir_medium)
//
//                txt_date.text = data.attributes?.updatedAgoInWords
            }
        }
    }

    interface Listener {

        fun onNotificationClick(notification: ConnectionItem)
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
