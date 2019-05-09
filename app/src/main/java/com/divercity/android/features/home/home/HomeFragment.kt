package com.divercity.android.features.home.home

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
import com.divercity.android.core.bus.RxBus
import com.divercity.android.core.bus.RxEvent
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.divercity.android.features.dialogs.HomeTabActionDialogFragment
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.home.HomeActivity
import com.divercity.android.features.home.home.adapter.HomeAdapter
import com.divercity.android.features.home.home.adapter.RecommendedAdapter
import com.divercity.android.features.home.home.adapter.recommended.RecommendedGroupViewHolder
import com.divercity.android.features.home.home.adapter.recommended.RecommendedJobViewHolder
import com.divercity.android.features.home.home.adapter.viewholder.QuestionsViewHolder
import com.divercity.android.features.jobs.jobs.adapter.JobsViewHolder
import com.divercity.android.model.Question
import com.divercity.android.model.position.GroupPositionModel
import com.divercity.android.model.position.JobPositionModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class HomeFragment : BaseFragment(), RetryCallback, JobApplyDialogFragment.Listener {

    lateinit var viewModel: HomeViewModel
    lateinit var recommendedViewModel: HomeRecommendedViewModel

    @Inject
    lateinit var homeAdapter: HomeAdapter

    @Inject
    lateinit var recommendedAdapter: RecommendedAdapter

    private var mIsRefreshing = false
    private var positionApplyClicked: Int = 0
    private var positionJoinRequest: Int = 0
    private var positionJoinClicked: Int = 0

    private var searchView: SearchView? = null
    private var searchItem: MenuItem? = null

    private lateinit var newMessageDisposable: Disposable

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

        recommendedViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(HomeRecommendedViewModel::class.java)
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

        homeAdapter.questionListener = object : QuestionsViewHolder.Listener {

            override fun onQuestionClick(position: Int, question: Question) {
                navigator.navigateToAnswerActivity(this@HomeFragment, question)
            }
        }

        homeAdapter.feedJobListener = object : JobsViewHolder.Listener {

            override fun onApplyClick(position: Int, job: JobResponse) {
                showJobApplyDialog(job.id)
            }

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id, job)
            }
        }

        list_jobs_questions.adapter = homeAdapter

        recommendedAdapter.setRetryCallback(RetryCallback {
            recommendedViewModel.retry()
        })

        recommendedAdapter.groupListener = object : RecommendedGroupViewHolder.Listener {

            override fun onGroupDiscarded(position: Int, group: GroupResponse) {
                viewModel.discardRecommendedGroup(GroupPositionModel(position, group))
            }

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

            override fun onJobDiscarded(position: Int, job: JobResponse) {
                viewModel.discardRecommendedJobs(JobPositionModel(position, job))
            }

            override fun onJobClick(job: JobResponse) {
                navigator.navigateToJobDescriptionSeekerActivity(activity!!, job.id, job)
            }

            override fun onApplyClick(position: Int, job: JobResponse) {
                positionApplyClicked = position
                showJobApplyDialog(job.id)
            }
        }

        homeAdapter.recommendedAdapter = recommendedAdapter
    }

    private fun showJobApplyDialog(jobId: String?) {
        val dialog = JobApplyDialogFragment.newInstance(jobId!!)
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
            if (!mIsRefreshing || it?.status == Status.ERROR || it?.status == Status.SUCCESS)
                recommendedAdapter.setNetworkState(it)
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
//                    recommendedAdapter.updatePositionOnJoinRequest(positionJoinRequest)
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
//                    recommendedAdapter.updatePositionOnJoinRequest(positionJoinRequest)
                }
            }
        })

        viewModel.listState.observe(viewLifecycleOwner, Observer { parcelable ->
            list_jobs_questions.layoutManager?.onRestoreInstanceState(parcelable)
        })
    }

    override fun retry() {
        recommendedViewModel.retry()
        viewModel.retry()
    }

    private fun initSwipeToRefresh() {

        swipe_list_main.apply {
            setOnRefreshListener {
                mIsRefreshing = true
                homeAdapter.onRefresh()
                viewModel.refresh()
                recommendedAdapter.onRefresh()
                recommendedViewModel.refresh()
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
                if (query != null && query.isNotEmpty())
                    navigator.navigateToSearch(this@HomeFragment, query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
//                menu.findItem(R.id.logout).isVisible = false
//                menu.findItem(R.id.about).isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
//                menu.findItem(R.id.logout).isVisible = true
//                menu.findItem(R.id.about).isVisible = true
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
        recommendedAdapter.updatePositionOnJobApplied(positionApplyClicked)
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

    override fun onDestroyView() {
        super.onDestroyView()

        searchView?.setOnQueryTextListener(null)
        searchItem?.setOnActionExpandListener(null)
        searchItem = null

        viewModel.listState.value = list_jobs_questions.layoutManager?.onSaveInstanceState()
        viewModel.onDestroyView()
        if (!newMessageDisposable.isDisposed) newMessageDisposable.dispose()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUnreadMessagesCount()
    }
}