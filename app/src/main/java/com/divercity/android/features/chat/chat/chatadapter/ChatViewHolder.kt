package com.divercity.android.features.chat.chat.chatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.data.entity.company.response.Photos
import com.divercity.android.features.apollo.FetchJobFromViewHolderUseCase
import com.divercity.android.features.apollo.FetchJobReloadedUseCase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_job_chat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


class ChatViewHolder
private constructor(
    itemView: View,
    val listener: Listener?,
    val adapterListener: ChatAdapter.Listener,
    private val fetchJobFromViewHolderUseCase: FetchJobFromViewHolderUseCase,
    private val fetchJobReloadedUseCase: FetchJobReloadedUseCase
) :
    RecyclerView.ViewHolder(itemView) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun bindTo(
        position: Int,
        currentUserId: String,
        data: ChatMessageResponse?,
        next: ChatMessageResponse?
    ) {
        data?.let {
            itemView.apply {
                txt_msg.text = it.message
                txt_time.text = Util.getStringAppTimeWithString(it.messageCreatedAt)

                if (currentUserId == it.fromUserId.toString())
                    txt_name.text = context.getString(R.string.me)
                else
                    txt_name.text = it.fromUsername

                GlideApp.with(itemView)
                    .load(data.fromUserAvatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img_user)

                if (next != null) {
                    if (Util.areDatesSameDay(it.messageCreatedAt, next.messageCreatedAt)) {
                        itemView.txt_date.visibility = View.GONE

                        if (it.fromUserId == next.fromUserId) {
                            itemView.img_user.visibility = View.GONE
                            itemView.txt_name.visibility = View.GONE
                        } else {
                            itemView.img_user.visibility = View.VISIBLE
                            itemView.txt_name.visibility = View.VISIBLE
                        }
                    } else {
                        itemView.txt_date.text =
                            Util.getStringDateWithServerDate(it.messageCreatedAt)
                        itemView.txt_date.visibility = View.VISIBLE
                        itemView.img_user.visibility = View.VISIBLE
                        itemView.txt_name.visibility = View.VISIBLE
                    }
                } else {
                    itemView.txt_date.text = Util.getStringDateWithServerDate(it.messageCreatedAt)
                    itemView.txt_date.visibility = View.VISIBLE
                    itemView.img_user.visibility = View.VISIBLE
                    itemView.txt_name.visibility = View.VISIBLE
                }

                if (it.embeddedAttachmentType == "Job") {
                    cardview_msg_picture.visibility = View.GONE

                    itemView.lay_job.item_jobs_txt_title.text = ""
                    itemView.lay_job.item_jobs_txt_company.text = ""
                    itemView.lay_job.item_jobs_txt_place.text = ""

                    val job = adapterListener.getJobFetched(it.embeddedAttachmentId!!)
                    if (job == null) {
                        fetchJobReloadedUseCase.invoke(
                            FetchJobReloadedUseCase.Params.forJob(
                                it.embeddedAttachmentId!!,
                                position,
                                itemView.lay_job,
                                data
                            )
                        ) {
                            it.either({

                            }, { my_data ->
                                adapterListener.onJobFetched(my_data.position, my_data.job.Job())
                            })
                        }
                    } else {
                        lay_job.visibility = View.VISIBLE
                        itemView.lay_job.item_jobs_txt_title.text = job.title()
                        val photos = Gson().fromJson(
                            job.employer()?.employer_photos() as String,
                            Photos::class.java)

                        GlideApp.with(itemView)
                            .load(photos.medium)
                            .into(item_jobs_img)

                        itemView.lay_job.item_jobs_txt_company.text = job.employer()?.name()
                        itemView.lay_job.item_jobs_txt_place.text = job.location_display_name()

                        job.is_applied_by_current?.also { isApplied ->
                            if (!isApplied) {
                                btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_apply))
                                btn_job_action.setOnClickListener {
                                    listener?.onJobApply(job.id())
                                }
                            } else {
                                btn_job_action.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.btn_applied))
                                btn_job_action.setOnClickListener(null)
                            }
                        }

                        itemView.lay_job.setOnClickListener {
                            listener?.onJobClick(job.id())
                        }
                    }
                } else {
                    lay_job.visibility = View.GONE

                    if (it.getCheckedPicture() != null) {
                        GlideApp.with(itemView)
                            .load(data.picture)
                            .into(itemView.img_msg_picture)
                        itemView.cardview_msg_picture.visibility = View.VISIBLE
                        itemView.cardview_msg_picture.setOnClickListener {
                            listener?.onImageTap(data.picture!!)
                        }
                    } else {
                        itemView.cardview_msg_picture.visibility = View.GONE
                        itemView.cardview_msg_picture.setOnClickListener(null)
                    }
                }
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
            adapterListener: ChatAdapter.Listener,
            fetchJobFromViewHolderUseCase: FetchJobFromViewHolderUseCase,
            fetchJobReloadedUseCase: FetchJobReloadedUseCase
        ): ChatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
            return ChatViewHolder(
                view,
                listener,
                adapterListener,
                fetchJobFromViewHolderUseCase,
                fetchJobReloadedUseCase
            )
        }
    }

    interface Listener {

        fun onImageTap(imageUrl: String)

        fun onJobClick(jobId : String)

        fun onJobApply(jobId : String)
    }
}
