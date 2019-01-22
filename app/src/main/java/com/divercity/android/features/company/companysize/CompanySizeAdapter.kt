package com.divercity.android.features.company.companysize
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import kotlinx.android.synthetic.main.item_text.view.*
import javax.inject.Inject

/**
 * Created by lucas on 20/02/2018.
 */

class CompanySizeAdapter @Inject
constructor() : RecyclerView.Adapter<CompanySizeAdapter.Holder>() {

    private var list: List<CompanySizeResponse>? = null
    var listener: Listener? = null

    fun submitList(list: List<CompanySizeResponse>?) {
        list?.let {
            this.list = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list!![position]
        holder.bindTo(item, listener)
    }

    override fun getItemCount(): Int {
        return if (this.list != null) this.list!!.size else 0
    }

    interface Listener {

        fun onCompanySizeClick(size: CompanySizeResponse)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(size: CompanySizeResponse, listener: Listener?) {
            itemView.item_text.text = size.attributes?.name
            itemView.setOnClickListener {
                listener?.onCompanySizeClick(size)
            }
        }
    }
}