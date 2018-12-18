package com.divercity.app.features.onboarding.selectage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class SelectAgeFragment : BaseFragment() {

    lateinit var viewModel: SelectAgeViewModel

    @Inject
    lateinit var adapter: SelectAgeAdapter

    private var currentProgress: Int = 50

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectAgeFragment {
            val fragment = SelectAgeFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectAgeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupView() {
        include_search.visibility = View.GONE

        list.adapter = adapter

        btn_continue.setOnClickListener {
            if (adapter.ageRangeSelected != null) {
                viewModel.updateUserProfile(adapter.ageRangeSelected)
            } else
                navigateToNext(false)
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupHeader() {
        include_header.apply {
            currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_agerange)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigateToNext(false)
            }
        }
    }

    private fun navigateToNext(shouldIncrement: Boolean) {
        navigator.navigateToNextOnboarding(activity!!,
                viewModel.accountType,
                currentProgress,
                shouldIncrement
        )
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message ?: "Error")
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigateToNext(true)
                }
            }
        })
    }
}
