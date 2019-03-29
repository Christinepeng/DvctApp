package com.divercity.android.features.home.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.divercity.android.BuildConfig
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.divercity.android.features.dialogs.HomeTabActionDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.groups.answers.model.Question
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.home.home.adapter.HomeAdapter
import com.divercity.android.features.home.home.adapter.recommended.RecommendedAdapter
import com.divercity.android.features.home.home.adapter.recommended.RecommendedGroupViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedJobViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class HomeFragment : BaseFragment(), RetryCallback, JobApplyDialogFragment.Listener {

    lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var homeAdapter: HomeAdapter

    @Inject
    lateinit var recommendedAdapter: RecommendedAdapter

    private var mIsRefreshing = false
    private var positionApplyClicked: Int = 0
    private var positionJoinRequest: Int = 0
    private var positionJoinClicked: Int = 0

    private lateinit var newMessageDisposable : Disposable

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newMessageDisposable = RxBus.listen(RxEvent.OnNewMessageReceived::class.java).subscribe {
            viewModel.fetchUnreadMessagesCount()
        }
        setupToolbar()
        setupEvents()
        initAdapters()
        initSwipeToRefresh()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as HomeActivity).apply {
            supportActionBar?.let {
                it.setTitle(R.string.divercity)
                it.setDisplayHomeAsUpEnabled(true)
                it.setHomeAsUpIndicator(R.drawable.icon_inbox)
            }
        }
    }

    private fun initAdapters() {
        homeAdapter.setRetryCallback(this)

        homeAdapter.questionListener = QuestionsViewHolder.Listener {
            navigator.navigateToAnswerActivity(
                this, Question(
                    id = it.id,
                    authorProfilePicUrl = it.attributes.authorInfo.avatarThumb,
                    authorName = it.attributes.authorInfo.name,
                    createdAt = it.attributes.createdAt,
                    question = it.attributes.text,
                    groupTitle =  it.attributes.group[0].title,
                    questionPicUrl = it.attributes.pictureMain
                )
            )
        }

        recommendedAdapter.groupListener = object : RecommendedGroupViewHolder.Listener {

            override fun onGroupRequestJoinClick(position: Int, group: GroupResponse) {
                positionJoinRequest = position
                viewModel.requestToJoinGroup(group)
            }

            override fun onGroupClick(group: GroupResponse) {
                navigator.navigateToGroupDetail(this@HomeFragment, group)
            }

            override fun onGroupJoinClick(position: Int, group: GroupResponse) {
                positionJoinClicked = position
                viewModel.joinGroup(group)
            }
        }

        recommendedAdapter.jobListener = object : RecommendedJobViewHolder.Listener {

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id, job)
            }

            override fun onApplyClick(position: Int, job: JobResponse) {
                positionApplyClicked = position
                showJobApplyDialog(job.id)
            }
        }

        homeAdapter.recommendedAdapter = recommendedAdapter

        homeAdapter.feedJobListener = object : JobsViewHolder.Listener {

            override fun onApplyClick(position: Int, job: JobResponse) {
                showJobApplyDialog(job.id)
            }

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id, job)
            }
        }

        list_main.layoutManager = LinearLayoutManager(context)
        list_main.adapter = homeAdapter
    }

    private fun showJobApplyDialog(jobId: String?) {
        val dialog = JobApplyDialogFragment.newInstance(jobId!!)
        dialog.show(childFragmentManager, null)
    }

    private fun subscribeToLiveData() {

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            if (!mIsRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                homeAdapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(viewLifecycleOwner, Observer { networkState ->

            homeAdapter.currentList?.let { pagedList ->

                if (networkState?.status != Status.LOADING)
                    mIsRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    lay_no_groups.visibility = View.VISIBLE
                    (btn_fab as View).visibility = View.GONE
                } else {
                    lay_no_groups.visibility = View.GONE
                    (btn_fab as View).visibility = View.VISIBLE
                }

                swipe_list_main.isRefreshing = mIsRefreshing
            }

            if (!mIsRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })

        viewModel.questionList.observe(this, Observer {
            homeAdapter.submitList(it)
        })

        viewModel.fetchRecommendedJobsGroupsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
                    recommendedAdapter.data = response.data
                }
            }
        })

        viewModel.joinGroupResponse.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { group ->
                when (group.status) {
                    Status.LOADING -> showProgress()

                    Status.ERROR -> {
                        hideProgress()
                        Toast.makeText(activity, group.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        hideProgress()
                        recommendedAdapter.updatePositionOnJoinGroup(positionJoinClicked)
                    }
                }
            }
        })

        viewModel.requestToJoinResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    recommendedAdapter.updatePositionOnJoinRequest(positionJoinRequest)
                }
            }
        })

        viewModel.fetchUnreadMessagesCountResponse.observe(viewLifecycleOwner, Observer { resource ->
            when (resource?.status) {
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
                Status.SUCCESS -> {
                    activity?.apply {
                        if (resource.data!! > 0) {
                            icon_notification.visibility = View.VISIBLE
                            icon_notification.text = resource.data.toString()
                        } else
                            icon_notification.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.listState.observe(this, Observer { parcelable ->
            list_main.layoutManager?.onRestoreInstanceState(parcelable)
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                mIsRefreshing = true
                viewModel.refresh()
            }

            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_home_fragment_toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                (activity as HomeActivity).logout()
                true
            }
            R.id.action_search -> {
                showToast()
                true
            }
            R.id.about -> {
                showAboutDialog()
                true
            }
            android.R.id.home -> {
                navigator.navigateToChatsActivity(activity!!)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupEvents() {
        btn_fab.setOnClickListener {
            showHomeTabActionDialog()
        }
        btn_create_edit_group.setOnClickListener {
            showToast()
        }
        btn_explore_groups.setOnClickListener {
            showToast()
        }
    }

    private fun showToast() {
        Toast.makeText(activity!!, "Coming soon", Toast.LENGTH_SHORT).show()
    }

    private fun showAboutDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.app_name),
            "Version: ".plus(BuildConfig.VERSION_NAME),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { customOneBtnDialogFragment.dismiss() }
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    override fun onSuccessJobApply() {

    }

    private fun showHomeTabActionDialog() {
        val dialog = HomeTabActionDialogFragment.newInstance()
        dialog.listener = object : HomeTabActionDialogFragment.Listener {

            override fun onNewGroup() {
                navigator.navigateToCreateGroupStep1(this@HomeFragment)
            }

            override fun onNewTopic() {
                navigator.navigateToCreateTopicActivity(this@HomeFragment, null)
            }
        }
        dialog.show(childFragmentManager, null)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.listState.value = list_main.layoutManager?.onSaveInstanceState()
        viewModel.onDestroyView()
        if (!newMessageDisposable.isDisposed) newMessageDisposable.dispose()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUnreadMessagesCount()
    }
}