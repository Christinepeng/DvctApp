package com.divercity.android.features.profile.tabfollowers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.features.chat.newchat.NewChatActivity
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: UserResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.userAttributes?.avatarMedium)
                        .apply(RequestOptions().circleCrop())
                        .into(img)

                if(itemView.context!! is NewChatActivity){
                    btn_follow.visibility = View.GONE
                    btn_direct_message.visibility = View.GONE
                } else {
                    btn_follow.setOnClickListener {
                        listener?.onUserFollow(data)
                    }

                    btn_direct_message.setOnClickListener {
                        listener?.onUserDirectMessage(data)
                    }
                }

                txt_name.text = it.userAttributes?.name
                txt_type.text = "@".plus(it.userAttributes?.nickname)
//                txt_school.text = "Need data"
            }
        }
    }

    interface Listener {

        fun onUserClick(user: UserResponse)

        fun onUserFollow(user: UserResponse)

        fun onUserDirectMessage(user: UserResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, listener)
        }
    }
}
