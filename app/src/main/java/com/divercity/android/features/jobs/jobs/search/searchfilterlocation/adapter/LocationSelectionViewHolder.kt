package com.divercity.android.features.jobs.jobs.search.searchfilterlocation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.location.LocationResponse
import kotlinx.android.synthetic.main.item_btn_text.view.*

class LocationSelectionViewHolder private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: LocationResponse?) {
        data?.let {
            itemView.apply {
                txt_title.text = data.attributes?.name?.plus(", ").plus(data.attributes?.countryName)
                setOnClickListener { listener?.onLocationClick(data) }
            }
        }
    }

    interface Listener {

        fun onLocationClick(location: LocationResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): LocationSelectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_btn_text, parent, false)
            return LocationSelectionViewHolder(view, listener)
        }
    }
}
