package com.divercity.android.features.home.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R

class WritePostViewHolder
private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo() {}

    companion object {

        fun create(
            parent: ViewGroup,
            onWritePost: () -> Unit
        ): WritePostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_write_post, parent, false)
            view.setOnClickListener {
                onWritePost()
            }
            return WritePostViewHolder(view)
        }
    }
}
