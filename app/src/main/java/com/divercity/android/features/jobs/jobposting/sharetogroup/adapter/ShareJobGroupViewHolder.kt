package com.divercity.android.features.jobs.jobposting.sharetogroup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.item_group_share.view.*

class ShareJobGroupViewHolder private constructor(itemView: View, private val listener: Listener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, isSelected: Boolean, data: GroupResponse) {
        itemView.apply {

            val urlMain = data.attributes.pictureMain
            GlideApp.with(itemView)
                .load(urlMain).into(itemView.item_group_img)

            item_group_txt_title.text = data.attributes.title
            item_group_txt_members.text =
                data.attributes.followersCount.toString().plus(" Members")

            btn_select_unselect.isSelected = isSelected
            btn_select_unselect.setOnClickListener {
                btn_select_unselect.isSelected = !itemView.btn_select_unselect.isSelected
                listener.onGroupShareClick(
                    position,
                    if (btn_select_unselect.isSelected) data.id else null
                )
            }
        }
    }

    interface Listener {
        fun onGroupShareClick(position: Int, groupId: String?)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): ShareJobGroupViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_share, parent, false)
            return ShareJobGroupViewHolder(view, listener)
        }
    }
}
