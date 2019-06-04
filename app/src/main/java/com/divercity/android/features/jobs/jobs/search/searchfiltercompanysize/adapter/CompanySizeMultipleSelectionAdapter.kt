package com.divercity.android.features.jobs.jobs.search.searchfiltercompanysize.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import javax.inject.Inject

/**
 * Created by lucas on 20/02/2018.
 */

class CompanySizeMultipleSelectionAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<CompanySizeResponse>? = null

    var selectedSizes = HashSet<CompanySizeResponse>()

    var onSelectUnselectSize: (() -> Unit)? = null

    var listener = object : CompanySizeMultipleSelectionViewHolder.Listener {

        override fun onSelectUnselectSizes(size: CompanySizeResponse, isSelected: Boolean) {
            if (isSelected)
                selectedSizes.add(size)
            else
                selectedSizes.remove(size)
            onSelectUnselectSize?.invoke()
        }
    }

    fun onSelectAll(){
        selectedSizes.clear()
        notifyDataSetChanged()
    }

    fun getSelectedSizesString() : String{
        return selectedSizes.joinToString {
            it.attributes?.name ?: ""
        }
    }

    fun submitList(list: List<CompanySizeResponse>?) {
        list?.let {
            this.list = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CompanySizeMultipleSelectionViewHolder.create(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list?.get(position)
        (holder as CompanySizeMultipleSelectionViewHolder).bindTo(
            selectedSizes.contains(item),
            item
        )
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
}