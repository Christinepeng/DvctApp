package com.divercity.android.features.chat.chat.useradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.item_user_mention.view.*

class UserMentionViewHolder
private constructor(itemView: View, private val listener : Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: User?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(data.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img)

            itemView.txt_name.text = data.name

            itemView.setOnClickListener {
                listener?.onUserClick(data)
            }
        }
    }

    interface Listener {
        fun onUserClick(data : User)
    }

    companion object {

        fun create(parent: ViewGroup, listener : Listener?): UserMentionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_mention, parent, false)
            return UserMentionViewHolder(view, listener)
        }
    }
}
