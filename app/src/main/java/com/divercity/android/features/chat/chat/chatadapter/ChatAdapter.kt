package com.divercity.android.features.chat.chat.chatadapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.divercity.android.features.apollo.FetchJobApolloUseCase
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class ChatAdapter @Inject
constructor(
    val sessionRepository: SessionRepository,
    private val fetchJobReloadedUseCase: FetchJobApolloUseCase
) : PagedListAdapter<ChatMessageEntityResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var retryCallback: RetryCallback? = null
    var chatListener: ChatViewHolder.Listener? = null
    var jobsFetched = SparseArray<FetchJobApolloUseCase.JobDataView>()

    private var adapterListener = object : Listener {

        override fun getJobFetchedByJobId(jobId: Int): FetchJobApolloUseCase.JobDataView? {
            return jobsFetched[jobId]
        }

        override fun onJobFetched(data: FetchJobApolloUseCase.JobDataView) {
            jobsFetched.put(data.id, data)
            ChatViewHolder.showJobData(
                data.view,
                data.job,
                data.errors,
                sessionRepository.isLoggedUserJobSeeker(),
                chatListener
            )
        }
    }

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder.create(
            parent,
            chatListener,
            adapterListener,
            fetchJobReloadedUseCase,
            sessionRepository.isLoggedUserJobSeeker()
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > position + 1)
            (holder as ChatViewHolder).bindTo(
                position,
                sessionRepository.getUserId(),
                getItem(position),
                getItem(position + 1)
            )
        else
            (holder as ChatViewHolder).bindTo(
                position,
                sessionRepository.getUserId(),
                getItem(position),
                null
            )
    }

    fun onDestroy() {
        fetchJobReloadedUseCase.cancel()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_text
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<ChatMessageEntityResponse>() {

            override fun areItemsTheSame(
                oldItem: ChatMessageEntityResponse,
                newItem: ChatMessageEntityResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ChatMessageEntityResponse,
                newItem: ChatMessageEntityResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {

        fun onJobFetched(data: FetchJobApolloUseCase.JobDataView)

        fun getJobFetchedByJobId(jobId: Int): FetchJobApolloUseCase.JobDataView?
    }
}