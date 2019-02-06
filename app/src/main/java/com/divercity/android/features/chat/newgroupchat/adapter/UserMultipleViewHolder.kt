package com.divercity.android.features.chat.newgroupchat.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.item_user_group_chat.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*

class UserMultipleViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected : Boolean, data: UserResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.userAttributes?.avatarMedium)
                        .apply(RequestOptions().circleCrop())
                        .into(include_img_desc.img)

                btn_select.isSelected  = isSelected
                btn_select.setOnClickListener {
                    btn_select.isSelected = !btn_select.isSelected
                    listener?.onSelectUnselectUser(data, btn_select.isSelected)
                }

                include_img_desc.txt_name.text = it.userAttributes?.name
                include_img_desc.txt_type.text = "@".plus(it.userAttributes?.nickname)
//                txt_school.text = "Need data"
            }
        }
    }

    interface Listener {

        fun onSelectUnselectUser(user: UserResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): UserMultipleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_group_chat, parent, false)
            return UserMultipleViewHolder(
                view,
                listener
            )
        }
    }
}
