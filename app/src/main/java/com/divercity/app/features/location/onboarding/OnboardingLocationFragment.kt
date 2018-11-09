package com.divercity.app.features.location.onboarding

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.company.base.SelectCompanyViewModel
import com.divercity.app.features.company.base.adapter.CompanyAdapter
import com.divercity.app.features.location.base.SelectLocationFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingLocationFragment : BaseFragment(), SelectLocationFragment.Listener {

    lateinit var viewModel: SelectCompanyViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingLocationFragment {
            val fragment = OnboardingLocationFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectCompanyViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectLocationFragment.newInstance()).commit()
        setupHeader()
    }

    private fun setupHeader() {
        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_country)

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

    override fun onLocationChoosen(location: LocationResponse) {
        navigator.navigateToNextOnboarding(
                activity!!,
                viewModel.accountType,
                currentProgress,
                true
        )
    }
}
