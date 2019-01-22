package com.divercity.android.features.onboarding.selectinterests

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.view.*
import javax.inject.Inject

class SelectInterestsFragment : BaseFragment() {

    lateinit var viewModel: SelectInterestsViewModel

    @Inject
    lateinit var adapter: SelectInterestsAdapter

    var currentProgress: Int = 0

    private var selectedIds: List<String>? = null

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectInterestsFragment {
            val fragment = SelectInterestsFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
                ViewModelProviders.of(this, viewModelFactory)[SelectInterestsViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupView() {
        btn_continue.visibility = View.GONE
        include_search.visibility = View.GONE

        list.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.setListener(listener)
        list.adapter = adapter
    }

    private fun setupHeader() {
        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_interests)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                if (btn_skip.text == getString(R.string.skip)) {
                    navigator.navigateToNextOnboarding(
                            activity!!,
                            viewModel.getAccountType(),
                            currentProgress,
                            false)
                } else {
                    viewModel.followInterests(selectedIds!!)
                }
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchInterestsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    showErrorDialog(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    adapter.setList(response.data)
                }
            }
        })

        viewModel.followInterestsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToNextOnboarding(
                            activity!!,
                            viewModel.getAccountType(),
                            currentProgress,
                            true)
                }
            }
        })
    }

    private fun showErrorDialog(msg : String?) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
                "Ups!",
                msg ?: "Error",
                getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { viewModel.fetchInterests() }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private val listener = SelectInterestsAdapter.Listener {
        selectedIds = it
        if (it.size >= 3) {
            include_header.btn_skip.setText(R.string.done)
        } else {
            include_header.btn_skip.setText(R.string.skip)
        }
    }
}