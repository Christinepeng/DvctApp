package com.divercity.android.features.company.companyaddadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.item_user_admin.view.*

class AdminSelectedViewHolder
private constructor(itemView: View, private val listener : Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: UserResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(data.userAttributes?.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img)

            itemView.txt_name.text = data.userAttributes?.name

            itemView.btn_remove_img.setOnClickListener {
                listener?.onAdminRemoved(data)
            }
        }
    }

    interface Listener {
        fun onAdminRemoved(data : UserResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener : Listener?): AdminSelectedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_admin, parent, false)
            return AdminSelectedViewHolder(view, listener)
        }
    }
}
