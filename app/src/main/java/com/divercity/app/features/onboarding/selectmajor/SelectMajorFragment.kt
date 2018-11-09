package com.divercity.app.features.onboarding.selectmajor


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.RetryCallback
import com.divercity.app.data.Status
import com.divercity.app.features.onboarding.selectmajor.adapter.MajorAdapter
import com.divercity.app.features.onboarding.selectmajor.adapter.MajorViewHolder
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectMajorFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectMajorViewModel

    @Inject
    lateinit var adapter: MajorAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectMajorFragment {
            val fragment = SelectMajorFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectMajorViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_continue.visibility = View.GONE
        viewModel.fetchMajors(this, null)
        adapter.setRetryCallback(this)
        adapter.setListener(listener)
        list.adapter = adapter
        subscribeToPaginatedLiveData()
        subscribeToUpdateUserLiveData()
        setupHeader()
    }

    private fun setupHeader() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                var toSearch: String? = include_search.edtxt_search.getText().toString()

                if (toSearch == "")
                    toSearch = null

                viewModel.fetchMajors(this@SelectMajorFragment, toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_school)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(
                        activity!!,
                        viewModel.accountType,
                        currentProgress,
                        false
                )
            }
        }
    }

    private fun subscribeToUpdateUserLiveData() {
//        viewModelJobs.updateUserProfileResponse.observe(this, Observer { school ->
//            when (school?.status) {
//                Status.LOADING -> showProgress()
//
//                Status.ERROR -> {
//                    hideProgress()
//                    Toast.makeText(activity, school.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    hideProgress()
//                    //TODO navigateToNextProfile
//                }
//            }
//        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedMajorList.observe(this, Observer {
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

    override fun retry() {
        viewModel.retry()
    }

    private val listener: MajorViewHolder.Listener = MajorViewHolder.Listener {
        //        viewModelJobs.updateUserProfile(it)
        navigator.navigateToNextOnboarding(activity!!,
                viewModel.accountType,
                currentProgress,
                true
        )
    }
}