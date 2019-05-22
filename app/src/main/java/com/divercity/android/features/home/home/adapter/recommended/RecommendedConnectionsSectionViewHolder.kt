package com.divercity.android.features.home.home.adapter.recommended

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.features.home.home.adapter.RecommendedConnectionsAdapter
import kotlinx.android.synthetic.main.item_list_recommended_connection.view.*

class RecommendedConnectionsSectionViewHolder
private constructor(itemView: View, val adapter: RecommendedConnectionsAdapter) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo() {}

    companion object {

        fun create(
            parent: ViewGroup,
            adapter: RecommendedConnectionsAdapter
        ): RecommendedConnectionsSectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_list_recommended_connection, parent, false)
            view.list_recommended.adapter = adapter
            return RecommendedConnectionsSectionViewHolder(
                view,
                adapter
            )
        }
    }
}
