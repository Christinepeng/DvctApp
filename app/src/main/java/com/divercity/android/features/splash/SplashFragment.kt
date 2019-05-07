package com.divercity.android.features.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import io.branch.referral.Branch
import timber.log.Timber


/**
 * Created by lucas on 24/10/2018.
 */

class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel

    companion object {

        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_splash

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
        subscribeToLiveData()
    }

    override fun onStart() {
        super.onStart()
        initBranch()
    }

    private fun initBranch() {
//      Deep link routing
        Branch.getInstance().initSession({ referringParams, error ->
            if (error == null) {
                Timber.e("BRANCH SDK $referringParams")

                if (referringParams.optBoolean("+clicked_branch_link", false)) {
                    viewModel.checkRouteDeepLink(referringParams)
                } else {
                    if (requireActivity().isTaskRoot)
                        viewModel.checkRouteNoDeepLink()
                    else
                        finish()
                }
            } else {
                Timber.e("BRANCH SDK ${error.message}")
                viewModel.showBranchIOErrorDialog.call()
            }
        }, requireActivity().intent?.data, requireActivity())
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(viewLifecycleOwner, Observer { listResource ->
            if (Status.ERROR == listResource?.status)
                showErrorDialog(
                    listResource.message ?: "Error",
                    ::fetchCurrentUserDataToCheckUserTypeDefined
                )
        })

        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
            navigator.navigateToHomeActivity(requireActivity())
//            navigator.navigateToSelectGroupActivity(requireActivity(), 25)
//            navigator.navigateToSelectCompanyActivity(requireActivity(), 20)
            finish()
        })

        viewModel.showBranchIOErrorDialog.observe(viewLifecycleOwner, Observer {
            showErrorDialog(
                "Network error",
                ::initBranch
            )
        })

        viewModel.navigateToSelectUserType.observe(viewLifecycleOwner, Observer {
            navigator.navigateToSelectUserTypeActivity(requireActivity())
            finish()
        })

        viewModel.navigateToEnterEmail.observe(viewLifecycleOwner, Observer {
            navigator.navigateToEnterEmailActivity(requireActivity())
            finish()
        })

        viewModel.navigateToGroupDetail.observe(viewLifecycleOwner, Observer {
            navigator.navigateToGroupDetail(this, it.toString())
            finish()
        })

        viewModel.navigateToResetPassword.observe(viewLifecycleOwner, Observer {
            navigator.navigateToResetPassword(this, it)
            finish()
        })
    }

    private fun finish() {
        requireActivity().finish()
    }

    private fun showErrorDialog(msg: String, action: () -> Unit) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            "Ups!",
            msg,
            getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { action() }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun fetchCurrentUserDataToCheckUserTypeDefined() {
        viewModel.fetchCurrentUserDataToCheckUserTypeDefined()
    }
}