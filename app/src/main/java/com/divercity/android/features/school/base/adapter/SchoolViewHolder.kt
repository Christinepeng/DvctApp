package com.divercity.android.features.school.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.model.School
import kotlinx.android.synthetic.main.item_img_text.view.*

class SchoolViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: School?) {
        data?.let {
            itemView.apply {
                item_img_txt_img.visibility = View.GONE
                item_img_txt_txt.text = it.name
                setOnClickListener {
                    listener?.onSchoolChosen(data)
                }
            }
        }
    }

    interface Listener {

        fun onSchoolChosen(school: School)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): SchoolViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_img_text, parent, false)
            return SchoolViewHolder(view, listener)
        }
    }
}
