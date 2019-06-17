package com.divercity.android.features.major.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.model.Major
import kotlinx.android.synthetic.main.item_img_text.view.*

class MajorViewHolder
private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: Major?) {
        data?.let {
            itemView.apply {
                item_img_txt_img.visibility = View.GONE
                item_img_txt_txt.text = it.name
                setOnClickListener {
                    listener?.onMajorChosen(data)
                }
            }
        }
    }

    interface Listener {

        fun onMajorChosen(major: Major)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): MajorViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_img_text, parent, false)
            return MajorViewHolder(view, listener)
        }
    }
}
