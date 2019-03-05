package com.divercity.android.features.groups.answers.answeradapter

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.repository.session.SessionRepository
import javax.inject.Inject

class AnswerAdapter @Inject
constructor(val sessionRepository: SessionRepository) :
    PagedListAdapter<AnswerResponse, RecyclerView.ViewHolder>(userDiffCallback) {

    private var retryCallback: RetryCallback? = null
    var answerListener: AnswerViewHolder.Listener? = null

    fun setRetryCallback(retryCallback: RetryCallback) {
        this.retryCallback = retryCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerViewHolder.create(parent, answerListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > position + 1)
            (holder as AnswerViewHolder).bindTo(getItem(position), getItem(position + 1))
        else
            (holder as AnswerViewHolder).bindTo(getItem(position), null)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_text
    }

    companion object {

        private val userDiffCallback = object : DiffUtil.ItemCallback<AnswerResponse>() {

            override fun areItemsTheSame(
                oldItem: AnswerResponse,
                newItem: AnswerResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AnswerResponse,
                newItem: AnswerResponse
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}