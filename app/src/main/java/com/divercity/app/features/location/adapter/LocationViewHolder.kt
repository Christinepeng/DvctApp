package com.divercity.app.features.location.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.location.LocationResponse
import kotlinx.android.synthetic.main.item_text.view.*

class LocationViewHolder private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: LocationResponse?) {
        data?.let {
            itemView.item_text.text = data.attributes?.name?.plus(", ").plus(data.attributes?.countryName)
            itemView.setOnClickListener { listener?.onLocationClick(data) }

        }
    }

    interface Listener {

        fun onLocationClick(location: LocationResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): LocationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_text, parent, false)
            return LocationViewHolder(view, listener)
        }
    }
}
