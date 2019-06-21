package com.divercity.android.features.home.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.BuildConfig
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.divercity.android.features.dialogs.HomeTabActionDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.home.home.adapter.HomeAdapter
import com.divercity.android.features.home.home.adapter.RecommendedAdapter
import com.divercity.android.features.home.home.adapter.RecommendedConnectionsAdapter
import com.divercity.android.features.home.home.adapter.recommended.RecommendedConnectionViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedGroupViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedJobViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.JobFeedViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.model.Question
import com.divercity.android.model.position.GroupPosition
import com.divercity.android.model.position.JobPosition
import com.divercity.android.model.position.UserPosition
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


/**
 * Created by lucas on 25/10/2018.
 */


class HomeFragment : BaseFragment() {

    lateinit var viewModel: HomeViewModel
    private lateinit var recommendedViewModel: HomeRecommendedViewModel
    lateinit var recommendedConnectionsViewModel: HomeRecommendedConnectionsViewModel

    @Inject
    lateinit var homeAdapter: HomeAdapter

    @Inject
    lateinit var recommendedAdapter: RecommendedAdapter

    @Inject
    lateinit var recommendedConnectionsAdapter: RecommendedConnectionsAdapter

    private var mIsRefreshing = false
    private var positionApplyClicked: Int = 0
    private var lastRecommendedUserPositionTap: Int = 0

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

//    private lateinit var newMessageDisposable: Disposable

    companion object {

        const val REQUEST_CODE_USER = 200

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(HomeViewModel::class.java)

        recommendedViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(HomeRecommendedViewModel::class.java)

        recommendedConnectionsViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(HomeRecommendedConnectionsViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        homeAdapter.showRecommendedSection = viewModel.showRecommendedSection.value!!

        homeAdapter.setRetryCallback(RetryCallback {
            viewModel.retry()
        })

        homeAdapter.questionListener = object : QuestionsViewHolder.Listener {

            override fun onQuestionClick(position: Int, question: Question) {
                navigator.navigateToAnswerActivity(this@HomeFragment, question)
            }
        }

        homeAdapter.feedJobListener = object : JobFeedViewHolder.Listener {
            override fun onApplyClick(jobPos: JobPosition) {
                showJobApplyDialog(jobPos, false)
            }

            override fun onSaveUnsaveClick(save: Boolean, jobPos: JobPosition) {
                viewModel.saveUnsaveJob(save, jobPos)
            }

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(requireActivity(), job.id, job)
            }
        }

        recommendedAdapter.setRetryCallback(RetryCallback {
            recommendedAdapter.onRefreshRetry()
            recommendedViewModel.retry()
        })

        recommendedAdapter.groupListener = object : RecommendedGroupViewHolder.Listener {
            override fun onGroupRequestJoinClick(groupPos: GroupPosition) {
                viewModel.requestToJoinGroup(groupPos)
            }

            override fun onGroupJoinClick(groupPos: GroupPosition) {
                viewModel.joinGroup(groupPos)
            }

            override fun onGroupClick(groupPos: GroupPosition) {
                navigator.navigateToGroupDetail(this@HomeFragment, groupPos.group)
            }

            override fun onGroupDiscarded(groupPos: GroupPosition) {
                viewModel.discardRecommendedGroup(groupPos)
            }
        }

        recommendedAdapter.jobListener = object : RecommendedJobViewHolder.Listener {

            override fun onJobDiscarded(position: Int, job: JobResponse) {
                viewModel.discardRecommendedJobs(JobPosition(position, job))
            }

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(requireActivity(), job.id, job)
            }

            override fun onApplyClick(position: Int, job: JobResponse) {
                positionApplyClicked = position
                showJobApplyDialog(JobPosition(position, job), true)
            }
        }

        homeAdapter.recommendedAdapter = recommendedAdapter

        recommendedConnectionsAdapter.setRetryCallback(RetryCallback {
            recommendedConnectionsAdapter.onRefreshRetry()
            recommendedConnectionsViewModel.retry()
        })

        recommendedConnectionsAdapter.listener = object : RecommendedConnectionViewHolder.Listener {

            override fun onUserClick(userPosition: UserPosition) {
                lastRecommendedUserPositionTap = userPosition.position
                navigator.navigateToOtherUserProfileForResult(
                    this@HomeFragment,
                    null,
                    userPosition.user,
                    REQUEST_CODE_USER
                )
            }

            override fun onConnectClick(userPosition: UserPosition) {
                recommendedConnectionsViewModel.connectToUser(userPosition)
            }

            override fun onUserDiscarded(userPosition: UserPosition) {
                recommendedConnectionsViewModel.discardRecommendedConnection(userPosition)
            }
        }

        homeAdapter.recommendedConnectionsAdapter = recommendedConnectionsAdapter

        homeAdapter.onWritePost = {
            navigator.navigateToCreateNewPost(this@HomeFragment)
        }

        list_jobs_questions.adapter = homeAdapter

    }

    private fun showJobApplyDialog(jobPos: JobPosition, isRecommended: Boolean) {
        val dialog = JobApplyDialogFragment.newInstance(jobPos.job.id!!)
        dialog.listener = object : JobApplyDialogFragment.Listener {

            override fun onSuccessJobApply() {
                if (isRecommended)
                    recommendedAdapter.updatePositionOnJobApplied(jobPos.position)
                else
                    homeAdapter.updatePositionOnJobApplied(jobPos.position)
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun subscribeToLiveData() {

        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            homeAdapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            if (!mIsRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                homeAdapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->

            homeAdapter.currentList?.let { pagedList ->

                if (networkState?.status != Status.LOADING)
                    mIsRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
                    (btn_fab as View).visibility = View.GONE
                } else {
                    (btn_fab as View).visibility = View.VISIBLE
                }

                swipe_list_main.isRefreshing = mIsRefreshing
            }

            if (!mIsRefreshing)
                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })

        recommendedViewModel.pagedList.observe(viewLifecycleOwner, Observer {
            recommendedAdapter.submitList(it)
        })

        recommendedViewModel.networkState().observe(viewLifecycleOwner, Observer {
            recommendedAdapter.setNetworkState(it)

            if ((it?.status == Status.ERROR || it?.status == Status.SUCCESS) &&
                ((recommendedAdapter.currentList?.size == 0) ||
                        (recommendedAdapter.currentList?.size != 0 && !homeAdapter.showRecommendedSection))
            ) {
                viewModel.showRecommendedSection.value = true
                homeAdapter.notifyItemChanged(0)
            }
        })

        recommendedViewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->

            recommendedAdapter.currentList?.let { pagedList ->

                //                if (networkState?.status != Status.LOADING)
//                    mIsRefreshing = false

                if (networkState?.status == Status.SUCCESS && pagedList.size == 0) {
//                    (btn_fab as View).visibility = View.GONE
                } else {
//                    (btn_fab as View).visibility = View.VISIBLE
                }

//                swipe_list_main.isRefreshing = mIsRefreshing
            }

//            if (!mIsRefreshing)
//                swipe_list_main.isEnabled = networkState?.status == Status.SUCCESS
        })

        recommendedConnectionsViewModel.pagedList.observe(viewLifecycleOwner, Observer {
            recommendedConnectionsAdapter.submitList(it)
        })

        recommendedConnectionsViewModel.networkState().observe(viewLifecycleOwner, Observer {
            recommendedConnectionsAdapter.setNetworkState(it)
        })

        recommendedConnectionsViewModel.refreshState().observe(viewLifecycleOwner, Observer { _ ->

        })

        viewModel.joinGroupResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    recommendedAdapter.updatePositionOnJoinGroup(response.data!!)
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
                    recommendedAdapter.updatePositionOnJoinGroupRequest(response.data!!)
                }
            }
        })

        viewModel.fetchUnreadMessagesCountResponse.observe(
            viewLifecycleOwner,
            Observer { resource ->
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

        viewModel.discardRecommendedGroupsResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                        showProgressNoBk()
                    }

                    Status.ERROR -> {
                        hideProgressNoBk()
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        hideProgressNoBk()
                        recommendedAdapter.onGroupDiscarded(response.data!!)
//                    recommendedAdapter.updatePositionOnJoinGroupRequest(positionJoinRequest)
                    }
                }
            })

        viewModel.discardRecommendedJobsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgressNoBk()
                }

                Status.ERROR -> {
                    hideProgressNoBk()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgressNoBk()
                    recommendedAdapter.onJobDiscarded(response.data!!)
//                    recommendedAdapter.updatePositionOnJoinGroupRequest(positionJoinRequest)
                }
            }
        })

        viewModel.jobSaveUnsaveResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                Status.LOADING -> {
                    showProgressNoBk()
                }

                Status.ERROR -> {
                    hideProgressNoBk()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgressNoBk()
                    homeAdapter.notifyItemChanged(response.data?.position!! - 1)
                }
            }
        })

        viewModel.listState.observe(viewLifecycleOwner, Observer { parcelable ->
            list_jobs_questions.layoutManager?.onRestoreInstanceState(parcelable)
        })

        viewModel.showRecommendedSection.observe(viewLifecycleOwner, Observer {
            homeAdapter.showRecommendedSection = it
        })

        recommendedConnectionsViewModel.connectUserResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response?.status) {
                    Status.LOADING -> {
                        showProgressNoBk()
                    }

                    Status.ERROR -> {
                        hideProgressNoBk()
                        showToast(response.message)
                    }

                    Status.SUCCESS -> {
                        hideProgressNoBk()
                        recommendedConnectionsAdapter.notifyItemChanged(response.data?.position!!)
                    }
                }
            })

        recommendedConnectionsViewModel.discardRecommendedUserResponse
            .observe(
                viewLifecycleOwner,
                Observer { response ->
                    when (response?.status) {
                        Status.LOADING -> {
                            showProgressNoBk()
                        }

                        Status.ERROR -> {
                            hideProgressNoBk()
                            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            hideProgressNoBk()
                            recommendedConnectionsAdapter.onUserDiscarded(response.data!!)
                        }
                    }
                })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                mIsRefreshing = true
                homeAdapter.onRefreshRetry()
                viewModel.refresh()
                recommendedAdapter.onRefreshRetry()
                recommendedViewModel.refresh()
                viewModel.showRecommendedSection.value = false
                recommendedConnectionsAdapter.onRefreshRetry()
                recommendedConnectionsViewModel.refresh()
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
        searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.search)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.length == 1) {
                    navigator.navigateToSearch(this@HomeFragment, newText)
                }
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                (activity as HomeActivity).logout()
                true
            }
            R.id.about -> {
                showAboutDialog()
                true
            }
            android.R.id.home -> {
                navigator.navigateToChatsActivity(requireActivity())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupEvents() {

        btn_fab.setOnClickListener {
            showHomeTabActionDialog()
        }
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

    private fun showHomeTabActionDialog() {
        val dialog = HomeTabActionDialogFragment.newInstance()
        dialog.listener = object : HomeTabActionDialogFragment.Listener {

            override fun onNewGroup() {
                navigator.navigateToCreateGroupStep1(this@HomeFragment)
            }

            override fun onNewPost() {
                navigator.navigateToCreateNewPost(this@HomeFragment)
//                navigator.navigateToCreateTopicActivity(this@HomeFragment, null)
            }
        }
        dialog.show(childFragmentManager, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        searchView?.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null

        viewModel.listState.value = list_jobs_questions.layoutManager?.onSaveInstanceState()
        viewModel.onDestroyView()
//        if (!newMessageDisposable.isDisposed) newMessageDisposable.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_USER) {
            recommendedConnectionsAdapter.notifyItemChanged(lastRecommendedUserPositionTap)
        }
    }

    override fun onResume() {
        super.onResume()
        /* When user search and press back the search is collapse*/
        searchItem?.collapseActionView()
        viewModel.fetchUnreadMessagesCount()
    }
}