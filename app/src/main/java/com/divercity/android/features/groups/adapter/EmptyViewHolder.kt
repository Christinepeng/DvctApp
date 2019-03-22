package com.divercity.android.features.groups.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R

class EmptyViewHolder
private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo() {

    }

    companion object {

        fun create(parent: ViewGroup): EmptyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_empty, parent, false)
            return EmptyViewHolder(view)
        }
    }
}
