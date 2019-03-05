package com.divercity.android.features.groups.groupdetail.conversation

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.questions.QuestionResponse
import com.divercity.android.features.groups.answers.model.Question
import com.divercity.android.features.groups.groupdetail.conversation.adapter.GroupConversationAdapter
import com.divercity.android.features.groups.groupdetail.conversation.adapter.GroupConversationViewHolder
import kotlinx.android.synthetic.main.fragment_list_refresh.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class GroupConversationFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: GroupConversationViewModel

    @Inject
    lateinit var adapter: GroupConversationAdapter

    private var isListRefreshing = false

    private var groupId: String? = null

    companion object {

        private const val PARAM_GROUP_ID = "paramGroupId"

        fun newInstance(groupId: String): GroupConversationFragment {
            val fragment = GroupConversationFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_GROUP_ID, groupId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_refresh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(GroupConversationViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        groupId = arguments?.getString(PARAM_GROUP_ID)
        groupId?.let {
            viewModel.fetchConversations(this, it, null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeToRefresh()
        initAdapter()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        adapter.setListener(object : GroupConversationViewHolder.Listener {

            override fun onConversationClick(question: QuestionResponse) {
                navigator.navigateToAnswerActivity(
                    this@GroupConversationFragment, Question(
                        id = question.id,
                        authorProfilePicUrl = question.attributes.authorInfo.avatarThumb,
                        authorName = question.attributes.authorInfo.name,
                        createdAt = question.attributes.createdAt,
                        question = question.attributes.text,
                        groupTitle =  question.attributes.group[0].title,
                        questionPicUrl = question.attributes.pictureMain
                    )
                )            }
        })
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedConversationList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            if (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                isListRefreshing = true
                viewModel.refresh()
            }
            isEnabled = false
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark),
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun retry() {
        viewModel.retry()
    }
}