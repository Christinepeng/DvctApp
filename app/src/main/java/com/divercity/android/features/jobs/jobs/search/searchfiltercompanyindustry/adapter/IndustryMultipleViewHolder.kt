package com.divercity.android.features.jobs.jobs.search.searchfiltercompanyindustry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.industry.IndustryResponse
import kotlinx.android.synthetic.main.item_selection_text.view.*

class IndustryMultipleViewHolder private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(selectStatus: Boolean, data: IndustryResponse?) {
        data?.let {
            itemView.apply {
                txt_title.text = data.attributes?.name

                btn_select_unselect.isSelected  = selectStatus

                setOnClickListener {
                    btn_select_unselect.isSelected = !btn_select_unselect.isSelected
                    listener?.onSelectUnselectIndustry(data, btn_select_unselect.isSelected)
                }
            }
        }
    }

    interface Listener {

        fun onSelectUnselectIndustry(industry: IndustryResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): IndustryMultipleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_selection_text, parent, false)
            return IndustryMultipleViewHolder(view, listener)
        }
    }
}
