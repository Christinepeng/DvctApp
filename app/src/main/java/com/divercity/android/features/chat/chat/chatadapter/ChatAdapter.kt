package com.divercity.android.features.chat.chat.chatadapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.chat.messages.ChatMessageResponse
import com.divercity.android.features.apollo.FetchJobFromViewHolderUseCase
import com.divercity.android.features.apollo.FetchJobReloadedUseCase
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class ChatAdapter @Inject
constructor(
    val sessionRepository: SessionRepository,
    private val fetchJobFromViewHolderUseCase: FetchJobFromViewHolderUseCase,
    private val fetchJobReloadedUseCase: FetchJobReloadedUseCase
) : PagedListAdapter<ChatMessageResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var retryCallback: RetryCallback? = null
    var chatListener: ChatViewHolder.Listener? = null
    var jobsFetched = HashMap<Int, FetchJobReloadedUseCase.JobDataView>()

    private var adapterListener = object : Listener {

        override fun onJobFetched(position: Int, job: FetchJobReloadedUseCase.JobDataView) {
            jobsFetched[position] = job
            getItem(position)?.attachment = job
            notifyItemChanged(position)
        }

        override fun getJobFetchedByPosition(position: Int): FetchJobReloadedUseCase.JobDataView? {
            return jobsFetched[position]
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
            fetchJobFromViewHolderUseCase,
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
        fetchJobFromViewHolderUseCase.cancel()
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_text
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<ChatMessageResponse>() {

            override fun areItemsTheSame(
                oldItem: ChatMessageResponse,
                newItem: ChatMessageResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ChatMessageResponse,
                newItem: ChatMessageResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {

        fun onJobFetched(position: Int, job: FetchJobReloadedUseCase.JobDataView)

        fun getJobFetchedByPosition(position: Int): FetchJobReloadedUseCase.JobDataView?
    }
}