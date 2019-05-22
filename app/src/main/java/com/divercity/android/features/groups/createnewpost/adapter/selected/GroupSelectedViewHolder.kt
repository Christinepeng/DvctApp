package com.divercity.android.features.groups.createnewpost.adapter.selected

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.group.GroupResponse
import kotlinx.android.synthetic.main.item_admin_company.view.*

class GroupSelectedViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: GroupResponse?) {
        data?.let {
            GlideApp.with(itemView)
                .load(it.attributes.pictureMain)
                .apply(RequestOptions().circleCrop())
                .into(itemView.img)

            itemView.txt_name.text = it.attributes.title

            itemView.btn_remove_img.setOnClickListener {
                listener?.onGroupRemoved(data)
            }
        }
    }

    interface Listener {
        fun onGroupRemoved(data: GroupResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupSelectedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_admin_company, parent, false)
            return GroupSelectedViewHolder(view, listener)
        }
    }
}
