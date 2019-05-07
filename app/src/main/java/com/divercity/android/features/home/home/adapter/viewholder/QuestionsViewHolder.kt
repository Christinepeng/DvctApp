package com.divercity.android.features.home.home.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.model.Question
import com.divercity.android.repository.session.SessionRepository
import kotlinx.android.synthetic.main.item_question.view.*

class QuestionsViewHolder
private constructor(itemView: View, private val listener: Listener?, val sessionRepository: SessionRepository) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: Question?) {

        data?.let { q ->
            itemView.apply {
                item_quest_txt_groupname.text = q.groupTitle
                item_quest_txt_author_time.text =
                    " · " + data.authorName + " · " +
                            Util.getTimeAgoWithStringServerDate(data.createdAt)
                item_quest_txt_question.text = q.question

                val questionUrlPicture = q.checkedPicture()
                if (questionUrlPicture != null) {
                    GlideApp.with(itemView)
                        .load(questionUrlPicture)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                item_quest_cardview_pic_main.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                item_quest_cardview_pic_main.visibility = View.VISIBLE
                                return false
                            }
                        })
                        .into(item_quest_img_main)
                } else {
                    item_quest_cardview_pic_main.visibility = View.GONE
                }

                GlideApp.with(itemView)
                    .load(q.authorProfilePicUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(item_group_img)

                if (q.lastAnswer != null) {
                    GlideApp.with(itemView)
                        .load(q.lastAnswerAuthorPicture)
                        .into(item_quest_img_answer)

                    item_quest_txt_answer.text =
                        data.lastAnswerAuthorName + ": " + data.lastAnswer

                } else {
                    GlideApp.with(itemView)
                        .load(sessionRepository.getUserAvatarUrl())
                        .into(item_quest_img_answer)

                    item_quest_txt_answer.text = ""
                    item_quest_txt_answer.hint = "Be the first to comment"
                }

                setOnClickListener {
                    listener?.onQuestionClick(position,q)
                }
            }
        }
    }

    interface Listener {

        fun onQuestionClick(position: Int, question: Question)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?, sessionRepository: SessionRepository): QuestionsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_question, parent, false)
            return QuestionsViewHolder(view, listener, sessionRepository)
        }
    }
}
