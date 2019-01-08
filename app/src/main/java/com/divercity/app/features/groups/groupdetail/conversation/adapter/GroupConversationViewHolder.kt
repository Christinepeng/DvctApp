package com.divercity.app.features.groups.groupdetail.conversation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.core.utils.Util
import com.divercity.app.data.entity.job.response.JobResponse
import com.divercity.app.data.entity.questions.QuestionResponse
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
                    .into(itemView.item_quest_img_main)

            itemView.item_quest_txt_author_name.text = it.attributes.authorInfo.name
            itemView.item_quest_txt_answer.text = it.attributes.text
            itemView.item_quest_txt_author_time.text = Util.getStringDateTimeWithServerDate(it.attributes.createdAt)
        }
    }

    interface Listener {

        fun onConversationClick(job: JobResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): GroupConversationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_group_conversation, parent, false)
            return GroupConversationViewHolder(view, listener)
        }
    }
}
