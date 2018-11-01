package com.divercity.app.features.home.jobs.jobs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.entity.job.JobResponse
import com.divercity.app.features.home.jobs.jobs.adapter.JobsAdapter
import com.divercity.app.features.home.jobs.jobs.adapter.JobsViewHolder
import kotlinx.android.synthetic.main.fragment_list.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class JobsListFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: JobsListViewModel

    @Inject
    lateinit var adapter: JobsAdapter

    companion object {

        fun newInstance(): JobsListFragment {
            return JobsListFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(JobsListViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
//        subscribeToSchoolLiveData()
//        setupHeader()
    }

    private fun subscribeToSchoolLiveData() {
//        viewModel.updateUserProfileResponse.observe(this, Observer { school ->
//            when (school?.status) {
//                Status.LOADING -> showProgress()
//
//                Status.ERROR -> {
//                    hideProgress()
//                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                    navigator.navigateToNextOnboarding(activity!!,
//                            viewModel.accountType,
//                            currentProgress,
//                            true
//                    )
//                }
//            }
//        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedJobsList?.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            adapter.setNetworkState(it!!)
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    private val listener : JobsViewHolder.Listener = object: JobsViewHolder.Listener {

        override fun onSaveClick(position: Int, job: JobResponse) {

        }

        override fun onJobClick(job: JobResponse) {

        }
    }
}