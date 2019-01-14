package com.divercity.app.features.home.home.feed.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.home.Recommended
import com.divercity.app.features.home.home.recommended.RecommendedAdapter
import kotlinx.android.synthetic.main.item_list_recommended.view.*

class RecommendedViewHolder
private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: Recommended?, adapter: RecommendedAdapter) {
        data?.let {
            adapter.data = data.list
            itemView.list_recommended.adapter = adapter
        }
    }

    companion object {

        fun create(parent: ViewGroup): RecommendedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_recommended, parent, false)
            return RecommendedViewHolder(view)
        }
    }
}
