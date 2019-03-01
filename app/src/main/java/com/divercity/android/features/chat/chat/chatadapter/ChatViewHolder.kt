package com.divercity.android.features.chat.chat.chatadapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.features.apollo.FetchJobFromViewHolderUseCase
import com.divercity.android.features.apollo.FetchJobReloadedUseCase
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
                    lay_job.visibility = View.VISIBLE
                    cardview_msg_picture.visibility = View.GONE

                    itemView.lay_job.item_jobs_txt_title.setText("")
                    itemView.lay_job.item_jobs_txt_company.setText("")
                    itemView.lay_job.item_jobs_txt_place.setText("")

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
                                //                            my_data.lay.item_jobs_txt_title.setText(my_data.job.Job().id())
//                            my_data.lay.item_jobs_txt_company.text = my_data.job.Job().title()
//                            my_data.lay.item_jobs_txt_place.text = my_data.chat.embeddedAttachmentId
//                            my_data.chat.attachment = my_data.job.Job()
                                adapterListener?.onJobFetched(my_data.position, my_data.job.Job())
                            })
                        }
                    } else {
                        itemView.lay_job.item_jobs_txt_title.text = job.title()
                        itemView.lay_job.item_jobs_txt_company.text = "Hello"
                    }

//                    scope.launch {
//                        val pol = itemView.lay_job
//                        val mydata = data
//                        pol.item_jobs_txt_title.setText("")
//                        pol.item_jobs_txt_company.setText("")
//                        pol.item_jobs_txt_place.setText("")
//                        fetchJobFromViewHolderUseCase.invoke(FetchJobFromViewHolderUseCase.Params.forJob(it.embeddedAttachmentId!!)) {
//                            it.either({
//
//                            }, {
//                                pol.item_jobs_txt_title.setText(it.Job().id())
//                                pol.item_jobs_txt_company.setText(
//                                    Integer.toHexString(
//                                        System.identityHashCode(
//                                            pol
//                                        )
//                                    ))
//                                pol.item_jobs_txt_place.setText(mydata.embeddedAttachmentId)
//
//                            })
//                        }
//                    }

//                    val pol = itemView.lay_job
//                    val mydata = data
//                    pol.item_jobs_txt_title.setText("")
//                    pol.item_jobs_txt_company.setText("")
//                    pol.item_jobs_txt_place.setText("")
//                    fetchJobFromViewHolderUseCase.invoke(
//                        FetchJobFromViewHolderUseCase.Params.forJob(
//                            it.embeddedAttachmentId!!
//                        )
//                    ) {
//                        it.either({
//
//                        }, {
//                            pol.item_jobs_txt_title.setText(it.Job().id())
//                            pol.item_jobs_txt_company.setText(
//                                Integer.toHexString(
//                                    System.identityHashCode(
//                                        pol
//                                    )
//                                )
//                            )
//                            pol.item_jobs_txt_place.setText(mydata.embeddedAttachmentId)
//
//                        })
//                    }
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
    }
}
