package com.divercity.android.features.location.base.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.location.LocationResponse
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
