package com.divercity.android.features.groups.answers.answeradapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import kotlinx.android.synthetic.main.item_answer.view.*

class AnswerViewHolder
private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: AnswerResponse?, next: AnswerResponse?) {
        data?.attributes?.let {
            itemView.apply {
                txt_msg.text = it.text
                txt_time.text = Util.getStringAppTimeWithDate(it.createdAt)
                txt_name.text = it.authorInfo?.name

                GlideApp.with(itemView)
                    .load(it.authorInfo?.avatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(img_user)

                if (next != null) {
                    if (Util.areDatesSameDay(it.createdAt, next.attributes?.createdAt)) {
                        txt_date.visibility = View.GONE

                        if (it.authorId == next.attributes?.authorId) {
                            itemView.img_user.visibility = View.GONE
                            itemView.txt_name.visibility = View.GONE
                        } else {
                            itemView.img_user.visibility = View.VISIBLE
                            itemView.txt_name.visibility = View.VISIBLE
                        }
                    } else {
                        itemView.txt_date.text = Util.getStringDateWithServerDate(it.createdAt)
                        itemView.txt_date.visibility = View.VISIBLE
                        itemView.img_user.visibility = View.VISIBLE
                        itemView.txt_name.visibility = View.VISIBLE
                    }
                } else {
                    itemView.txt_date.text = Util.getStringDateWithServerDate(it.createdAt)
                    itemView.txt_date.visibility = View.VISIBLE
                    itemView.img_user.visibility = View.VISIBLE
                    itemView.txt_name.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {

        fun create(parent: ViewGroup): AnswerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_answer, parent, false)
            return AnswerViewHolder(view)
        }
    }
}