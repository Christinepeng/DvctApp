package com.divercity.android.features.dialogs.recentdocuments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.document.DocumentResponse
import kotlinx.android.synthetic.main.item_text_subtext.view.*
import javax.inject.Inject

/**
 * Created by lucas on 20/02/2018.
 */

class RecentDocsDialogsAdapter @Inject
constructor() : RecyclerView.Adapter<RecentDocsDialogsAdapter.Holder>() {

    private var list: List<DocumentResponse>? = null
    var listener: Listener? = null

    fun submitList(list: List<DocumentResponse>?) {
        list?.let {
            this.list = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_text_subtext, parent, false)
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

        fun onDocClick(doc: DocumentResponse)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(doc: DocumentResponse, listener: Listener?) {
            itemView.item_text.text = doc.attributes?.name
            itemView.txt_created_at.text = itemView.context.getString(R.string.created_at, Util.getStringDateTimeWithServerDate(doc.attributes?.createdAt))
            itemView.setOnClickListener {
                listener?.onDocClick(doc)
            }
        }
    }
}