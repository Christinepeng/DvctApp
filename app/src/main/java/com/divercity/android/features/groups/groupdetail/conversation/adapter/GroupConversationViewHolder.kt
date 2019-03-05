package com.divercity.android.features.groups.groupdetail.conversation.adapter

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.questions.QuestionResponse
import kotlinx.android.synthetic.main.item_group_conversation.view.*

class GroupConversationViewHolder
private constructor(itemView: View, private val listener: Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: QuestionResponse?) {
        data?.let {
            GlideApp.with(itemView)
                    .load(data.attributes.authorInfo.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.item_group_img)

            GlideApp.with(itemView)
                    .load(data.attributes.pictureMain)
                    .listener(object: RequestListener<Drawable>{
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            itemView.item_quest_cardview_pic_main.visibility = View.VISIBLE
                            return false
                        }

                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            itemView.item_quest_cardview_pic_main.visibility = View.GONE
                            return false
                        }
                    })
                    .into(itemView.item_quest_img_main)

            itemView.item_quest_txt_author_name.text = it.attributes.authorInfo.name
            itemView.item_quest_txt_answer.text = it.attributes.text
            itemView.item_quest_txt_author_time.text = Util.getStringDateTimeWithServerDate(it.attributes.createdAt)
            itemView.setOnClickListener {
                listener?.onConversationClick(data)
            }
        }
    }

    interface Listener {

        fun onConversationClick(question: QuestionResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupConversationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_conversation, parent, false)
            return GroupConversationViewHolder(view, listener)
        }
    }
}
