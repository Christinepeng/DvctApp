package com.divercity.android.features.invitations.users.adapter

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

class UserSelectionViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected : Boolean, data: User?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.avatarMedium)
                        .apply(RequestOptions().circleCrop())
                        .into(include_img_desc.img)

                btn_select.isSelected  = isSelected
                btn_select.setOnClickListener {
                    btn_select.isSelected = !btn_select.isSelected
                    listener?.onSelectUnselectUser(data, btn_select.isSelected)
                }

                include_img_desc.txt_name.text = it.name
                include_img_desc.txt_subtitle1.text = it.occupation
            }
        }
    }

    interface Listener {

        fun onSelectUnselectUser(user: User, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): UserSelectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_selection, parent, false)
            return UserSelectionViewHolder(
                view,
                listener
            )
        }
    }
}
