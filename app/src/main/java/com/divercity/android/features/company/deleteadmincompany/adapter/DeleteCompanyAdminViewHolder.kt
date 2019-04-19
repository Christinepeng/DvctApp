package com.divercity.android.features.company.deleteadmincompany.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.company.companyadmin.response.CompanyAdminResponse
import kotlinx.android.synthetic.main.item_user_left_selection.view.*
import kotlinx.android.synthetic.main.view_user_image_desc.view.*

class DeleteCompanyAdminViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?,
    private val ownerId: String,
    private val currentUserId: String
) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected: Boolean, data: CompanyAdminResponse?) {
        data?.attributes?.user?.let {
            itemView.apply {

                GlideApp.with(this)
                    .load(it.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(include_img_desc.img)

                val adminId = data.attributes.user.id!!.toString()
                if (ownerId == adminId || currentUserId == adminId) {
                    btn_select.visibility = View.INVISIBLE
                } else {
                    btn_select.visibility = View.VISIBLE
                    btn_select.isSelected = isSelected
                    btn_select.setOnClickListener {
                        btn_select.isSelected = !btn_select.isSelected
                        listener?.onSelectUnselectUser(data, btn_select.isSelected)
                    }
                }

                if (it.occupation != null && it.occupation != "") {
                    include_img_desc.txt_subtitle1.visibility = View.VISIBLE
                    include_img_desc.txt_subtitle1.text = it.occupation
                } else {
                    include_img_desc.txt_subtitle1.visibility = View.GONE
                }

                include_img_desc.txt_name.text = it.name
                include_img_desc.txt_subtitle2.visibility = View.GONE
            }
        }
    }

    interface Listener {

        fun onSelectUnselectUser(user: CompanyAdminResponse, isSelected: Boolean)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
            ownerId: String,
            currentUserId: String
        ): DeleteCompanyAdminViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_left_selection, parent, false)
            return DeleteCompanyAdminViewHolder(
                view,
                listener,
                ownerId,
                currentUserId
            )
        }
    }
}
