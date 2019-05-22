package com.divercity.android.features.groups.yourgroups

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.adapter.GroupsViewHolder
import com.divercity.android.features.groups.yourgroups.adapter.GroupAdapter
import com.divercity.android.features.groups.yourgroups.adapter.YourGroupsAdapter
import com.divercity.android.features.groups.yourgroups.adapter.YourGroupsViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.features.search.ITabSearch
import com.divercity.android.model.Question
import com.divercity.android.model.position.GroupPosition
import kotlinx.android.synthetic.main.fragment_your_groups.*
import javax.inject.Inject

class YourGroupsFragment : BaseFragment(), RetryCallback, ITabSearch {

    lateinit var viewModel: YourGroupsViewModel

    @Inject
    lateinit var adapter: YourGroupsAdapter

    @Inject
    lateinit var groupAdapter: GroupAdapter

    private var isListRefreshing = false
    private var handlerSearch = Handler()

    companion object {

        fun newInstance(): YourGroupsFragment {
            return YourGroupsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_your_groups

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(YourGroupsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setupView()
        initSwipeToRefresh()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.fetchMyGroupsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    viewModel.showFollowedGroupSection.value = response.data!!.isNotEmpty()
                    groupAdapter.list = response.data
                }
            }
        })

        viewModel.showFollowedGroupSection.observe(viewLifecycleOwner, Observer {
            adapter.showFollowedGroupSection = it
        })
    }

    private fun setupView() {
        adapter.showFollowedGroupSection = viewModel.showFollowedGroupSection.value!!

        adapter.setRetryCallback(this)
        adapter.questionListener = listener

        groupAdapter.listener = object : GroupsViewHolder.Listener {
            override fun onGroupRequestJoinClick(groupPosition: GroupPosition) {}

            override fun onGroupJoinClick(groupPosition: GroupPosition) {}

            override fun onGroupClick(position: Int, group: GroupResponse) {
                navigator.navigateToGroupDetail(this@YourGroupsFragment, group)
            }
        }

        adapter.yourGroupsListener = object : YourGroupsViewHolder.Listener {

            override fun onSeeAll() {
                navigator.navigateToFollowedGroups(this@YourGroupsFragment)
            }
        }

        adapter.groupAdapter = groupAdapter
        list.adapter = adapter
    }

    override fun search(searchQuery: String?) {
//        viewModel.fetchData(viewLifecycleOwner, searchQuery)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            //            adapter.showFollowedGroupSection = viewModel.lastSearch.isNullOrEmpty()
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status != Status.LOADING)
                    isListRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_groups.visibility = View.VISIBLE
                else {
                    txt_no_groups.visibility = View.GONE
                }

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
                viewModel.showFollowedGroupSection.value = false
                viewModel.fetchMyGroups()
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

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun retry() {
        viewModel.fetchMyGroups()
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    private val listener = object : QuestionsViewHolder.Listener {

        override fun onQuestionClick(position: Int, question: Question) {
            navigator.navigateToAnswerActivity(this@YourGroupsFragment, question)
        }
    }
}
