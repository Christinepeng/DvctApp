package com.divercity.app.features.chat.chat.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.core.utils.Util
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import kotlinx.android.synthetic.main.item_chat.view.*

class ChatViewHolder
private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(currentUserId: String, data: ChatMessageResponse?, next: ChatMessageResponse?) {
        data?.let {
            itemView.txt_msg.text = it.message
            itemView.txt_time.text = Util.getStringAppTimeWithString(it.messageCreatedAt)

            if (currentUserId == it.fromUserId.toString()) {
                itemView.txt_name.text = "Me"
                itemView.img_user.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.img_blue_user
                    )
                )
            } else {
                itemView.txt_name.text = it.fromUsername
                itemView.img_user.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.img_orange_user
                    )
                )
            }

            if (next != null) {
                if (Util.areDatesSameDay(it.messageCreatedAt, next.messageCreatedAt)) {
                    itemView.txt_date.visibility = View.GONE

                    if (it.fromUserId == next.fromUserId) {
                        itemView.img_user.visibility = View.GONE
                        itemView.txt_name.visibility = View.GONE
                    } else {
                        itemView.img_user.visibility = View.VISIBLE
                        itemView.txt_name.visibility = View.VISIBLE
                    }
                } else {
                    itemView.txt_date.text = Util.getStringDateWithServerDate(it.messageCreatedAt)
                    itemView.txt_date.visibility = View.VISIBLE
                    itemView.img_user.visibility = View.VISIBLE
                    itemView.txt_name.visibility = View.VISIBLE
                }
            } else {
                itemView.txt_date.text = Util.getStringDateWithServerDate(it.messageCreatedAt)
                itemView.txt_date.visibility = View.VISIBLE
                itemView.img_user.visibility = View.VISIBLE
                itemView.txt_name.visibility = View.VISIBLE
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup): ChatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
            return ChatViewHolder(view)
        }
    }
}
