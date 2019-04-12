package com.divercity.android.features.company.companyadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import com.divercity.android.repository.session.SessionRepository
import kotlinx.android.synthetic.main.item_user.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*

class CompanyAdminViewHolder
private constructor(itemView: View,
                    private val listener: Listener?,
                    val sessionRepository: SessionRepository) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: CompanyAdminResponse?) {
        data?.attributes?.user?.let {
            itemView.apply {
                GlideApp.with(this)
                    .load(it.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(include_img_desc.img)

                when (it.connected) {
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

                if(sessionRepository.getUserId() == it.id.toString()){
                    btn_direct_message.visibility = View.GONE
                    btn_follow.visibility = View.GONE
                } else {
                    btn_direct_message.visibility = View.VISIBLE
                    btn_follow.visibility = View.VISIBLE
                    btn_direct_message.setOnClickListener {
                        listener?.onUserDirectMessage(data)
                    }
                }

                include_img_desc.txt_name.text = it.name

                if (it.occupation != null && it.occupation != "") {
                    include_img_desc.txt_subtitle1.visibility = View.VISIBLE
                    include_img_desc.txt_subtitle1.text = it.occupation
                } else {
                    include_img_desc.txt_subtitle1.visibility = View.GONE
                }

                include_img_desc.txt_subtitle2.visibility = View.GONE

                setOnClickListener {
                    listener?.onUserClick(it.id.toString())
                }
            }
        }
    }

    interface Listener {

        fun onUserClick(userId: String)

        fun onConnectUser(admin: CompanyAdminResponse, position: Int)

        fun onUserDirectMessage(admin: CompanyAdminResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?, sessionRepository: SessionRepository): CompanyAdminViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return CompanyAdminViewHolder(view, listener, sessionRepository)
        }
    }
}
