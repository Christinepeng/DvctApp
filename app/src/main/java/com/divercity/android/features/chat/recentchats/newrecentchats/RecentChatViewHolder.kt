package com.divercity.android.features.chat.recentchats.newrecentchats

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem
import kotlinx.android.synthetic.main.item_recent_chat.view.*

class RecentChatViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: ExistingUsersChatListItem?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(it.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img_user)

            itemView.txt_name.text = data.name
            itemView.txt_msg.text = data.lastMessage

            itemView.txt_date.text = Util.getStringDateTimeWithServerDate(data.lastMessageDate)

            itemView.setOnClickListener {
                listener?.onChatClick(data)
            }

            if(data.unreadMessageCount!! > 0){
                itemView.icon_notification.text = data.unreadMessageCount!!.toString()
                itemView.icon_notification.visibility = View.VISIBLE
            } else
                itemView.icon_notification.visibility = View.GONE
        }
    }

    interface Listener {

        fun onChatClick(chat: ExistingUsersChatListItem)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): RecentChatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_recent_chat, parent, false)
            return RecentChatViewHolder(
                view,
                listener
            )
        }
    }
}
