package com.divercity.android.features.picturessearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject

class PictureAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onPictureSelected: (PhotoEntityResponse) -> Unit

    var list: List<PhotoEntityResponse> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_image, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PictureViewHolder).bindTo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class PictureViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: PhotoEntityResponse?) {
            data?.let { d ->
                itemView.apply {

                    GlideApp.with(this)
                        .load(d.imageUrlsBySize?.regular)
                        .transform(RoundedCorners(16))
                        .into(img)

                    setOnClickListener {
                        onPictureSelected(d)
                    }
                }
            }
        }
    }
}