package com.divercity.android.features.onboarding.selectoccupation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class SelectOccupationFragment : BaseFragment() {

    lateinit var viewModel: SelectOccupationViewModel

    @Inject
    lateinit var adapter: SelectOccupationAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectOccupationFragment {
            val fragment = SelectOccupationFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_header_search_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectOccupationViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_continue.visibility = View.VISIBLE
        include_search.visibility = View.GONE

        list.adapter = adapter
        setupHeader()
        setupEvents()
        subscribeToLiveData()
    }

    private fun setupEvents(){

        btn_continue.setOnClickListener {
            val listStr = adapter.listSelected.toString()
            viewModel.updateUserProfile(listStr.substring(1, listStr.length - 1))
        }
    }

    private fun setupHeader() {

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_occupation)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(activity!!,
                        viewModel.accountType,
                        currentProgress,
                        false
                )
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToNextOnboarding(activity!!,
                            viewModel.accountType,
                            currentProgress,
                            true
                    )
                }
            }
        })
    }
}