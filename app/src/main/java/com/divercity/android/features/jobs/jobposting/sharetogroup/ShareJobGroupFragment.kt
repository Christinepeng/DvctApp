package com.divercity.android.features.jobs.jobposting.sharetogroup

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.jobs.jobposting.sharetogroup.adapter.ShareJobGroupAdapter
import kotlinx.android.synthetic.main.fragment_share_job_group.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class ShareJobGroupFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: ShareJobGroupViewModel

    @Inject
    lateinit var adapter: ShareJobGroupAdapter

    private var handlerSearch = Handler()

    companion object {
        private const val PARAM_JOB_ID = "paramJobId"

        fun newInstance(progress: String): ShareJobGroupFragment {
            val fragment = ShareJobGroupFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_JOB_ID, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_share_job_group

    fun initViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory)[ShareJobGroupViewModel::class.java]
        viewModel.jobId = arguments?.getString(PARAM_JOB_ID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupView()
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.setRetryCallback(this)
        list.adapter = adapter

        btn_share.setOnClickListener {
            if(adapter.jobsIds.size != 0)
                viewModel.shareJobs(adapter.jobsIds)
            else
                showToast("Select at least one group")
        }

        setupHeader()
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as ShareJobGroupActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.share_groups)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupHeader() {
        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                search(include_search.edtxt_search.text.toString())
                true
            } else
                false
        }
    }

    private fun subscribeToLiveData() {
        viewModel.shareJobGroupsResponse.observe(this, Observer { school ->
            when (school?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    activity!!.finish()
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            fetchFollowedGroups(query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedGroupList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState.observe(this, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let {
                    if (networkState.status == Status.SUCCESS && it.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    private fun fetchFollowedGroups(searchQuery: String?) {
        viewModel.fetchFollowedGroups(viewLifecycleOwner, searchQuery)
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun retry() {
        viewModel.retry()
    }
}