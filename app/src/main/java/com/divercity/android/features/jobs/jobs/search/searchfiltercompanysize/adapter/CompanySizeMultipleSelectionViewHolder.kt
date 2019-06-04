package com.divercity.android.features.jobs.jobs.search.searchfiltercompanysize.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import kotlinx.android.synthetic.main.item_selection_text.view.*

class CompanySizeMultipleSelectionViewHolder private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(selectStatus: Boolean, data: CompanySizeResponse?) {
        data?.let {
            itemView.apply {
                txt_title.text = it.attributes?.name

                btn_select_unselect.isSelected  = selectStatus

                setOnClickListener {
                    btn_select_unselect.isSelected = !btn_select_unselect.isSelected
                    listener?.onSelectUnselectSizes(data, btn_select_unselect.isSelected)
                }
            }
        }
    }

    interface Listener {

        fun onSelectUnselectSizes(size: CompanySizeResponse, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): CompanySizeMultipleSelectionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_selection_text, parent, false)
            return CompanySizeMultipleSelectionViewHolder(view, listener)
        }
    }
}
