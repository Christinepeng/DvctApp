package com.divercity.android.features.user.useradapter.groupadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.item_user_left_selection.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*

class GroupAdminViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?,
    private val ownerId: String,
    private val currentUserId: String
) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected: Boolean, data: User?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                    .load(it.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(include_img_desc.img)

                if (ownerId == data.id || currentUserId == data.id) {
                    btn_select.visibility = View.INVISIBLE
                } else {
                    btn_select.visibility = View.VISIBLE
                    btn_select.isSelected = isSelected
                    btn_select.setOnClickListener {
                        btn_select.isSelected = !btn_select.isSelected
                        listener?.onSelectUnselectUser(data, btn_select.isSelected)
                    }
                }

                include_img_desc.txt_name.text = it.name
                include_img_desc.txt_subtitle1.text = it.occupation
            }
        }
    }

    interface Listener {

        fun onSelectUnselectUser(user: User, isSelected: Boolean)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
            ownerId: String,
            currentUserId: String
        ): GroupAdminViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_left_selection, parent, false)
            return GroupAdminViewHolder(
                view,
                listener,
                ownerId,
                currentUserId
            )
        }
    }
}
