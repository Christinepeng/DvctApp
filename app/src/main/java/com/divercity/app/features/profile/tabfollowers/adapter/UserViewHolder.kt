package com.divercity.app.features.profile.tabfollowers.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.data.entity.login.response.LoginResponse
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: LoginResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                        .load(it.attributes?.avatarMedium)
                        .apply(RequestOptions().circleCrop())
                        .into(img)

                txt_name.text = it.attributes?.name
                txt_type.text = "Need data"
                txt_school.text = "Need data"
            }
        }
    }

    interface Listener {

        fun onUserClick(user: LoginResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): UserViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return UserViewHolder(view, listener)
        }
    }
}
