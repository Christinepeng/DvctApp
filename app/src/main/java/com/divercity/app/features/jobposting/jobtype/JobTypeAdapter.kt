package com.divercity.app.features.jobposting.jobtype

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.app.R
import com.divercity.app.data.entity.job.jobtype.JobTypeResponse
import kotlinx.android.synthetic.main.item_text.view.*
import javax.inject.Inject

/**
 * Created by lucas on 20/02/2018.
 */

class JobTypeAdapter @Inject
constructor() : RecyclerView.Adapter<JobTypeAdapter.Holder>() {

    private var list: List<JobTypeResponse>? = null
    var listener: Listener? = null

    fun submitList(list: List<JobTypeResponse>?) {
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

        fun onJobTypeClick(jobType: JobTypeResponse)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(jobType: JobTypeResponse, listener: Listener?) {
            itemView.item_text.text = jobType.attributes?.name
            itemView.setOnClickListener {
                listener?.onJobTypeClick(jobType)
            }
        }
    }
}