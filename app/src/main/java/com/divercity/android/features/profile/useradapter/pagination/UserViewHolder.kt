package com.divercity.android.features.profile.useradapter.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.newchat.NewChatActivity
import kotlinx.android.synthetic.main.item_user_action.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*

class UserViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: UserResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                    .load(it.userAttributes?.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(include_img_desc.img)

                if (itemView.context is NewChatActivity) {
                    btn_follow.visibility = View.GONE
                    btn_direct_message.visibility = View.GONE
                } else {

                    when (data.userAttributes?.connected) {
                        "connected" -> {
                            btn_follow.setImageResource(R.drawable.icon_connected)
                            btn_follow.setOnClickListener(null)
                        }
                        "requested" -> {
                            btn_follow.setImageResource(R.drawable.icon_followed)
                            btn_follow.setOnClickListener(null)
                        }
                        "pending_approval" -> {
                            btn_follow.setImageResource(R.drawable.icon_pending_approval)
                            btn_follow.setOnClickListener(null)
                        }
                        "not_connected" -> {
                            btn_follow.setImageResource(R.drawable.icon_request_connection)
                            btn_follow.isEnabled = true
                            btn_follow.setOnClickListener {
                                listener?.onConnectUser(data, position)
                                btn_follow.isEnabled = false
                            }
                        }
                    }

                    btn_direct_message.setOnClickListener {
                        listener?.onUserDirectMessage(data)
                    }
                }

                include_img_desc.txt_name.text = it.userAttributes?.name
                include_img_desc.txt_subtitle1.text = it.userAttributes?.occupation

                setOnClickListener {
                    listener?.onUserClick(data)
                }
            }
        }
    }

    interface Listener {

        fun onUserClick(user: UserResponse)

        fun onConnectUser(user: UserResponse, position : Int)

        fun onUserDirectMessage(user: UserResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_action, parent, false)
            return UserViewHolder(view, listener)
        }
    }
}