package com.divercity.android.features.groups.groupdetail.conversation

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.groups.groupdetail.GroupDetailViewModel
import com.divercity.android.features.groups.groupdetail.conversation.adapter.GroupConversationAdapter
import com.divercity.android.features.groups.groupdetail.conversation.adapter.GroupConversationViewHolder
import com.divercity.android.model.Question
import kotlinx.android.synthetic.main.fragment_group_conversation.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class GroupConversationFragment : BaseFragment(), RetryCallback {

    private lateinit var viewModel: GroupConversationViewModel
    private lateinit var groupDetailViewModel: GroupDetailViewModel

    @Inject
    lateinit var adapter: GroupConversationAdapter

    private var isListRefreshing = false

    companion object {

        fun newInstance(): GroupConversationFragment {
            return GroupConversationFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_group_conversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(GroupConversationViewModel::class.java)

        groupDetailViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(GroupDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.fetchConversations(groupDetailViewModel.groupId)
        subscribeToPaginatedLiveData()
    }

    private fun setupView(){
        txt_no_results.setText(R.string.no_group_conversation)

        swipe_list_main.apply {
            setOnRefreshListener {
                if (hasToShowConversations()) {
                    lay_must_be_member.visibility = View.GONE
                    isListRefreshing = true
                    viewModel.refresh()
                } else {
                    lay_must_be_member.visibility = View.VISIBLE
                    isRefreshing = false
                }
            }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }

        adapter.setRetryCallback(this)
        adapter.setListener(object : GroupConversationViewHolder.Listener {

            override fun onConversationClick(question: Question) {
                navigator.navigateToAnswerActivity(
                    this@GroupConversationFragment, question
                )
            }
        })
        list.adapter = adapter

        if (hasToShowConversations()) {
            swipe_list_main.isEnabled = false
            lay_must_be_member.visibility = View.GONE
        } else {
            swipe_list_main.isEnabled = true
            lay_must_be_member.visibility = View.VISIBLE
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (
                (!isListRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                &&
                hasToShowConversations()
            )
                adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->

            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else {
                    txt_no_results.visibility = View.GONE
                }

                swipe_list_main.isRefreshing = isListRefreshing
            }

            if (!isListRefreshing && hasToShowConversations())
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })
    }

    private fun hasToShowConversations(): Boolean {
        val group = groupDetailViewModel.groupLiveData.value
        return group!!.isPublic() || group.iAmAMemberOrAdmin()
    }

    override fun retry() {
        viewModel.retry()
    }
}