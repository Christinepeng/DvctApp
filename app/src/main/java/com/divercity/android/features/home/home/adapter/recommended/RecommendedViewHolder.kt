package com.divercity.android.features.home.home.adapter.recommended

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.features.home.home.adapter.RecommendedAdapter
import kotlinx.android.synthetic.main.item_list_recommended.view.*

class RecommendedViewHolder
private constructor(itemView: View, val adapter: RecommendedAdapter) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo() {
//        if(listSize != null && listSize != 0)
//            itemView.lay_recommended.visibility = View.VISIBLE
//        else
//            itemView.lay_recommended.visibility = View.GONE
    }

    companion object {

        fun create(
            parent: ViewGroup,
            adapter: RecommendedAdapter
        ): RecommendedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_recommended, parent, false)
            view.list_recommended.adapter = adapter
            return RecommendedViewHolder(
                view,
                adapter
            )
        }
    }
}
