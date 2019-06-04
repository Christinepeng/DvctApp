package com.divercity.android.features.chat.chat.chatadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.JobQuery
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.data.entity.company.response.Photos
import com.divercity.android.features.apollo.FetchJobApolloUseCase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_job_chat.view.*


class ChatViewHolder
private constructor(
    itemView: View,
    val listener: Listener?,
    private val adapterListener: ChatAdapter.Listener,
    private val fetchJobReloadedUseCase: FetchJobApolloUseCase,
    private val isLoggedUserJobSeeker: Boolean
) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(
        currentUserId: String,
        data: ChatMessageEntityResponse?,
        next: ChatMessageEntityResponse?
    ) {
        data?.let {
            itemView.apply {
                txt_msg.text = it.message
                txt_time.text = Util.getStringAppTimeWithString(it.messageCreatedAt)

                if (currentUserId == it.fromUserId.toString()) {
                    txt_name.text = context.getString(R.string.me)
                    txt_name.setOnClickListener(null)
                    img_user.setOnClickListener(null)
                } else {
                    txt_name.text = it.fromUsername
                    txt_name.setOnClickListener {
                        listener?.onNavigateToProfile(data.fromUserId!!.toString())
                    }
                    img_user.setOnClickListener {
                        listener?.onNavigateToProfile(data.fromUserId!!.toString())
                    }
                }

                GlideApp.with(itemView)
                    .load(data.fromUserAvatarMedium)
                    .apply(RequestOptions().circleCrop())
                    .into(img_user)

                if (next != null) {
                    if (Util.areDatesSameDay(it.messageCreatedAt, next.messageCreatedAt)) {
                        txt_date.visibility = View.GONE

                        if (it.fromUserId == next.fromUserId) {
                            img_user.visibility = View.GONE
                            txt_name.visibility = View.GONE
                        } else {
                            img_user.visibility = View.VISIBLE
                            txt_name.visibility = View.VISIBLE
                        }
                    } else {
                        txt_date.text =
                            Util.getStringDateWithServerDate(it.messageCreatedAt)
                        txt_date.visibility = View.VISIBLE
                        img_user.visibility = View.VISIBLE
                        txt_name.visibility = View.VISIBLE
                    }
                } else {
                    txt_date.text = Util.getStringDateWithServerDate(it.messageCreatedAt)
                    txt_date.visibility = View.VISIBLE
                    img_user.visibility = View.VISIBLE
                    txt_name.visibility = View.VISIBLE
                }

                if (it.embeddedAttachmentType == "Job") {
                    cardview_msg_picture.visibility = View.GONE

                    lay_job.item_jobs_txt_title.text = ""
                    lay_job.item_jobs_txt_company.text = ""
                    lay_job.item_jobs_txt_place.text = ""

                    lay_job.visibility = View.VISIBLE

                    val jobDataView =
                        adapterListener.getJobFetchedByJobId(it.embeddedAttachmentId!!.toInt())
                    if (jobDataView == null) {
                        fetchJobReloadedUseCase.invoke(
                            FetchJobApolloUseCase.Params.forJob(
                                it,
                                lay_job
                            )
                        ) {
                            it.either({ my_data ->
                                adapterListener.onJobFetched(my_data)
                            }, { my_data ->
                                adapterListener.onJobFetched(my_data)
                            })
                        }
                    } else {
                        showJobData(
                            lay_job,
                            jobDataView.job,
                            jobDataView.errors,
                            isLoggedUserJobSeeker,
                            listener
                        )
                    }
                } else {
                    lay_job.visibility = View.GONE

                    if (it.getCheckedPicture() != null) {
                        GlideApp.with(itemView)
                            .load(data.picture)
                            .into(itemView.img_msg_picture)
                        cardview_msg_picture.visibility = View.VISIBLE
                        cardview_msg_picture.setOnClickListener {
                            listener?.onImageTap(data.picture!!)
                        }
                    } else {
                        cardview_msg_picture.visibility = View.GONE
                        cardview_msg_picture.setOnClickListener(null)
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
            fetchJobReloadedUseCase: FetchJobApolloUseCase,
            isLoggedUserJobSeeker: Boolean
        ): ChatViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_chat, parent, false)
            return ChatViewHolder(
                view,
                listener,
                adapterListener,
                fetchJobReloadedUseCase,
                isLoggedUserJobSeeker
            )
        }

        fun showJobData(
            view: View?,
            cJob: JobQuery.Data?,
            errors: String?,
            isLoggedUserJobSeeker: Boolean,
            listener: Listener?
        ) {
            view?.apply {
                cJob.let {
                    item_jobs_txt_title.text = ""
                    item_jobs_txt_company.text = ""
                    item_jobs_txt_place.text = ""

                    if (errors.isNullOrEmpty()) {
                        val job = it!!.Job()
                        item_jobs_txt_title.text = job.title()

                        val photos = Gson().fromJson(
                            job.employer()?.employer_photos() as String,
                            Photos::class.java
                        )

                        GlideApp.with(this)
                            .load(photos.medium)
                            .into(item_jobs_img)

                        item_jobs_txt_company.text = job.employer()?.name()
                        item_jobs_txt_place.text = job.location_display_name()

                        if (isLoggedUserJobSeeker) {
                            btn_job_action.visibility = View.VISIBLE
                            job.is_applied_by_current?.also { isApplied ->
                                if (!isApplied) {
                                    btn_job_action.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            this.context,
                                            R.drawable.btn_apply
                                        )
                                    )
                                    btn_job_action.setOnClickListener {
                                        listener?.onJobApply(job.id())
                                    }
                                } else {
                                    btn_job_action.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            this.context,
                                            R.drawable.btn_applied
                                        )
                                    )
                                    btn_job_action.setOnClickListener(null)
                                }
                            }
                        } else {
                            btn_job_action.visibility = View.GONE
                        }

                        setOnClickListener {
                            listener?.onJobClick(job.id())
                        }

                        txt_errors.visibility = View.GONE
                        loading.visibility = View.GONE
                        card_view.visibility = View.VISIBLE
                        lay_desc.visibility = View.VISIBLE
                    } else {
                        loading.visibility = View.GONE
                        card_view.visibility = View.INVISIBLE
                        lay_desc.visibility = View.GONE
                        btn_job_action.visibility = View.GONE
                        txt_errors.visibility = View.VISIBLE
                        txt_errors.text = errors
                        setOnClickListener(null)
                    }
                }
            }
        }
    }

    interface Listener {

        fun onImageTap(imageUrl: String)

        fun onJobClick(jobId: String)

        fun onJobApply(jobId: String)

        fun onNavigateToProfile(userId: String)
    }
}
