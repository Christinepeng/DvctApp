package com.divercity.android.features.home.home.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.home.Recommended
import com.divercity.android.features.home.home.adapter.recommended.RecommendedAdapter
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
            return RecommendedViewHolder(
                view
            )
        }
    }
}
