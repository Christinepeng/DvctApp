package com.divercity.android.features.groups.createnewpost.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.item_group_small.view.*

class GroupsSmallViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected: Boolean, data: GroupResponse?) {
        data?.let {
            itemView.apply {
                GlideApp.with(this)
                    .load(data.attributes.pictureMain)
                    .into(item_group_img)

                item_group_txt_title.text = it.attributes.title

                img_checkmark.isSelected  = isSelected
                checkState(isSelected)

                setOnClickListener {
                    img_checkmark.isSelected = !img_checkmark.isSelected
                    checkState(img_checkmark.isSelected)
                    listener?.onSelectUnselectGroup(data, img_checkmark.isSelected)
                }
            }
        }
    }

    private fun checkState(isSelected: Boolean){
        itemView.apply {
            if(isSelected)
                img_checkmark.visibility = View.VISIBLE
            else
                img_checkmark.visibility = View.GONE
        }
    }

    interface Listener {

        fun onSelectUnselectGroup(group: GroupResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupsSmallViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_small, parent, false)
            return GroupsSmallViewHolder(view, listener)
        }
    }
}
