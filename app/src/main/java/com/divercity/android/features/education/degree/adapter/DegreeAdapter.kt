package com.divercity.android.features.education.degree.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.model.Degree
import kotlinx.android.synthetic.main.item_text.view.*
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class DegreeAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onDegreeSelected: (Degree) -> Unit

    var list: List<Degree> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_text, parent, false)
        return DegreeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DegreeViewHolder).bindTo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class DegreeViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: Degree?) {
            data?.let { d ->
                itemView.apply {
                    item_text.text = d.name

                    setOnClickListener {
                        onDegreeSelected(d)
                    }
                }
            }
        }
    }
}