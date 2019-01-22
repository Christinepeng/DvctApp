package com.divercity.android.features.onboarding.selectmajor


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.onboarding.selectmajor.adapter.MajorAdapter
import com.divercity.android.features.onboarding.selectmajor.adapter.MajorViewHolder
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectMajorFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectMajorViewModel

    @Inject
    lateinit var adapter: MajorAdapter

    var currentProgress: Int = 0

    private var handlerSearch = Handler()
    private var lastSearch: String? = null

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
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
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
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_major)

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

    private fun search(query: String?) {
        if (lastSearch != query) {
            handlerSearch.removeCallbacksAndMessages(null)
            handlerSearch.postDelayed({
                viewModel.fetchMajors(this@SelectMajorFragment, if (query == "") null else query)
                subscribeToPaginatedLiveData()
                lastSearch = query
            }, AppConstants.SEARCH_DELAY)
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
        //        viewModelJobs.updateUserProfileWithSelectedAgeRange(it)
        navigator.navigateToNextOnboarding(
            activity!!,
            viewModel.accountType,
            currentProgress,
            true
        )
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}