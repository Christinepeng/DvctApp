package com.divercity.android.features.ethnicity.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.model.Ethnicity
import kotlinx.android.synthetic.main.item_text.view.*
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class EthnicityAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onEthnicitySelected: (Ethnicity) -> Unit

    var list: List<Ethnicity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_text, parent, false)
        return EthnicityViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EthnicityViewHolder).bindTo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EthnicityViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: Ethnicity?) {
            data?.let { d ->
                itemView.apply {
                    item_text.text = d.name

                    setOnClickListener {
                        onEthnicitySelected(d)
                    }
                }
            }
        }
    }
}