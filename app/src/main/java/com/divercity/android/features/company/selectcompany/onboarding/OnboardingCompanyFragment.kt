package com.divercity.android.features.company.selectcompany.onboarding


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.selectcompany.base.SelectCompanyFragment
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.dialogs.ratecompany.RateCompanyDiversityDialogFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class OnboardingCompanyFragment : BaseFragment(), SelectCompanyFragment.Listener {

    lateinit var viewModel: OnboardingCompanyViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var currentProgress: Int = 0

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingCompanyFragment {
            val fragment = OnboardingCompanyFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[OnboardingCompanyViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
            R.id.fragment_fragment_container, SelectCompanyFragment.newInstance()
        ).commit()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupHeader() {
        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_company)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(requireActivity())
            }

            btn_skip.setOnClickListener {
                onSkip()
            }
        }
    }

    private fun onSkip() {
        navigator.navigateToNextOnboarding(
            requireActivity(),
            viewModel.getAccountType(),
            currentProgress,
            false
        )
    }

    private fun subscribeToLiveData() {
        viewModel.updateUserProfileResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showRateCompanyDialog(response.data!!)
                }
            }
        })
    }

    private fun showRateCompanyDialog(company: CompanyResponse) {
        val dialog = RateCompanyDiversityDialogFragment.newInstance(company)
        dialog.listener = object : RateCompanyDiversityDialogFragment.Listener {

            override fun onCompanyRated() {
                showToast("Company Rated Successfully")
                navigator.navigateToNextOnboarding(
                    requireActivity(),
                    viewModel.getAccountType(),
                    currentProgress,
                    true
                )
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCompanyChosen(company: CompanyResponse) {
        viewModel.updateUserProfile(company)
    }

    override fun onNoCurrentCompany() {
        onSkip()
    }
}
