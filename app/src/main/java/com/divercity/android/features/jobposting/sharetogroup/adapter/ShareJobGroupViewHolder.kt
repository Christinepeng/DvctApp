package com.divercity.android.features.jobposting.sharetogroup.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.item_group_share.view.*

class ShareJobGroupViewHolder private constructor(itemView: View, private val listener: Listener) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: GroupResponse) {
        try {
            itemView.item_group_img.visibility = View.VISIBLE
            val urlMain = data.attributes.pictureMain
            GlideApp.with(itemView)
                    .load(urlMain).into(itemView.item_group_img)
        } catch (e: NullPointerException) {
            itemView.item_group_img.visibility = View.GONE
        }

        itemView.item_group_txt_title.text = data.attributes.title
        itemView.item_group_txt_members.text = data.attributes.followersCount.toString().plus(" Members")

        itemView.btn_public_private.isSelected = data.selected
        itemView.btn_public_private.setOnClickListener {
            itemView.btn_public_private.isSelected = !itemView.btn_public_private.isSelected
            data.selected = itemView.btn_public_private.isSelected
            listener.onGroupShareClick(data, itemView.btn_public_private.isSelected)
        }

    }

    interface Listener {
        fun onGroupShareClick(group: GroupResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): ShareJobGroupViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_share, parent, false)
            return ShareJobGroupViewHolder(view, listener)
        }
    }
}
