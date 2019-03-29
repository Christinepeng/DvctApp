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
        //        Deep link routing
        Branch.getInstance().initSession({ referringParams, error ->
            if (error == null) {
                Timber.e("BRANCH SDK $referringParams")

                if (referringParams.getBoolean("+clicked_branch_link")) {
                    viewModel.checkRouteDeepLink(referringParams)
                } else {
                    viewModel.checkRouteNoDeepLink()
                }
            } else {
                Timber.e("BRANCH SDK ${error.message}")
                showErrorDialog(
                    "Network error",
                    ::initBranch
                )
            }
        }, activity?.intent?.data, activity)
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { listResource ->
            if (Status.ERROR == listResource?.status)
                showErrorDialog(
                    listResource.message ?: "Error",
                    ::fetchCurrentUserDataToCheckUserTypeDefined
                )
        })

        viewModel.navigateToHome.observe(this, Observer {
            navigator.navigateToHomeActivity(activity!!)
            activity!!.finish()
        })

        viewModel.navigateToSelectUserType.observe(this, Observer {
            navigator.navigateToSelectUserTypeActivity(activity!!)
            activity!!.finish()
        })

        viewModel.navigateToEnterEmail.observe(this, Observer {
            navigator.navigateToEnterEmailActivity(activity!!)
            activity!!.finish()
        })

        viewModel.navigateToGroupDetail.observe(this, Observer {
            navigator.navigateToGroupDetail(this, it.toString())
            activity!!.finish()
        })
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